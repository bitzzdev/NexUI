package com.nexui.ui;

import com.nexui.engine.AlignmentGuideEngine;
import com.nexui.engine.DesignModeManager;
import com.nexui.engine.RenderPipeline;
import com.nexui.model.ColorRGBA;
import com.nexui.model.ComponentStyle;
import com.nexui.model.LayoutProfile;
import com.nexui.model.Rect2i;
import com.nexui.model.UIComponent;
import com.nexui.registry.ProfileRegistry;
import com.nexui.ui.components.PropertyInspectorWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;

import java.util.List;

/**
 * Interactive Figma-style Design Mode screen allowing real-time UI element drag, drop, resize, styling, alignment guides, and toolbar controls.
 */
public class DesignModeScreen extends Screen {
    private final DesignModeManager manager = DesignModeManager.getInstance();
    private boolean isDragging = false;
    private List<AlignmentGuideEngine.AlignmentGuide> currentGuides = List.of();

    public DesignModeScreen() {
        super(Component.literal("NexUI Design Mode Studio"));
    }

    @Override
    protected void init() {
        super.init();
        manager.setDesignModeActive(true);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta) {
        // Note: Screen.extractRenderStateWithTooltipAndSubtitles already calls
        // extractBackground (which applies the blur) before this method, so we must
        // NOT call extractBackground again here (it would double-blur and crash).
        super.extractRenderState(context, mouseX, mouseY, delta);

        LayoutProfile activeProfile = ProfileRegistry.getInstance().getActiveProfile();

        // Render Canvas Grid if enabled
        if (activeProfile.isGridSnapEnabled()) {
            renderGrid(context, activeProfile.getGridSize());
        }

        // Render all UI components (hidden ones dimmed so every relocator stays visible)
        for (UIComponent component : activeProfile.getComponents().values()) {
            Rect2i bounds = component.getCurrentBounds();

            // Render component background panel
            RenderPipeline.renderStyledPanel(context, bounds, component.getStyle());

            // Label for editing identification
            String label = component.isVisible() ? component.getName() : component.getName() + " (hidden)";
            context.text(this.font, label, bounds.x() + 4, bounds.y() + 4, component.isVisible() ? ColorRGBA.WHITE.toARGB() : 0x66FFFFFF, false);
        }

        // Render Selection Outlines
        for (UIComponent selected : manager.getSelectedComponents()) {
            boolean isPrimary = selected.equals(manager.getPrimarySelectedComponent());
            RenderPipeline.renderSelectionOutline(context, selected.getCurrentBounds(), isPrimary);
        }

        // Render Smart Alignment Guides
        for (AlignmentGuideEngine.AlignmentGuide guide : currentGuides) {
            RenderPipeline.renderAlignmentGuide(context, guide, width, height);
        }

        // Render Top Studio Toolbar
        renderToolbar(context);

        // Render Property Inspector for Primary Selection
        UIComponent primary = manager.getPrimarySelectedComponent();
        if (primary != null) {
            PropertyInspectorWidget.renderInspector(context, primary, width, height);
        }
    }

    private void renderGrid(GuiGraphicsExtractor context, int gridSize) {
        int gridColor = new ColorRGBA(255, 255, 255, 15).toARGB();
        for (int x = 0; x < width; x += gridSize * 2) {
            context.fill(x, 0, x + 1, height, gridColor);
        }
        for (int y = 0; y < height; y += gridSize * 2) {
            context.fill(0, y, width, y + 1, gridColor);
        }
    }

    private Rect2i visibilityButtonBounds() {
        return new Rect2i(width - 358, 17, 120, 38);
    }

    private void renderToolbar(GuiGraphicsExtractor context) {
        ComponentStyle barStyle = new ComponentStyle();
        barStyle.setBackgroundColor(new ColorRGBA(18, 18, 24, 230));
        barStyle.setBorderColor(ColorRGBA.ACCENT_BLUE);
        barStyle.setBorderWidth(1);

        Rect2i barBounds = new Rect2i(10, 10, width - 240, 52);
        RenderPipeline.renderStyledPanel(context, barBounds, barStyle);

        String title = "NexUI Design Mode Studio  |  Profile: " + ProfileRegistry.getInstance().getActiveProfile().getName();
        context.text(this.font, title, 20, 20, ColorRGBA.ACCENT_CYAN.toARGB(), false);

        String actions = "[ESC] Save & Exit  |  [L] Lock  |  [Ctrl+Z] Undo  |  [Ctrl+Y] Redo  |  [Ctrl+C] Copy Style";
        context.text(this.font, actions, 20, 38, ColorRGBA.WHITE.toARGB(), false);

        Rect2i buttonBounds = visibilityButtonBounds();
        ComponentStyle buttonStyle = new ComponentStyle();
        buttonStyle.setBackgroundColor(new ColorRGBA(99, 102, 241, 230));
        buttonStyle.setBorderColor(ColorRGBA.ACCENT_BLUE);
        buttonStyle.setBorderWidth(1);
        RenderPipeline.renderStyledPanel(context, buttonBounds, buttonStyle);
        context.text(this.font, "Visibility", buttonBounds.x() + (buttonBounds.width() - this.font.width("Visibility")) / 2, buttonBounds.y() + 14, ColorRGBA.WHITE.toARGB(), false);
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClicked) {
        int mx = (int) event.x();
        int my = (int) event.y();

        if (visibilityButtonBounds().contains(mx, my)) {
            Minecraft.getInstance().gui.setScreen(new ElementVisibilityScreen());
            return true;
        }

        LayoutProfile activeProfile = ProfileRegistry.getInstance().getActiveProfile();
        UIComponent clickedComp = null;

        // Check component hit test
        for (UIComponent comp : activeProfile.getComponents().values()) {
            if (comp.isVisible() && comp.getCurrentBounds().contains(mx, my)) {
                clickedComp = comp;
                break;
            }
        }

        if (clickedComp != null) {
            manager.selectComponent(clickedComp.getId(), false);
            isDragging = true;
            return true;
        } else {
            manager.clearSelection();
        }

        return super.mouseClicked(event, doubleClicked);
    }

    @Override
    public boolean mouseDragged(MouseButtonEvent event, double deltaX, double deltaY) {
        if (isDragging && manager.getPrimarySelectedComponent() != null) {
            int dx = (int) deltaX;
            int dy = (int) deltaY;

            manager.moveSelectedComponents(dx, dy);

            // Compute smart alignment guides
            UIComponent primary = manager.getPrimarySelectedComponent();
            LayoutProfile activeProfile = ProfileRegistry.getInstance().getActiveProfile();
            if (activeProfile.isSmartGuidesEnabled() && primary != null) {
                AlignmentGuideEngine.AlignmentResult result = AlignmentGuideEngine.computeGuides(
                    primary, activeProfile.getComponents().values(), width, height
                );
                currentGuides = result.guides;
            }
            return true;
        }
        return super.mouseDragged(event, deltaX, deltaY);
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent event) {
        isDragging = false;
        currentGuides = List.of();
        return super.mouseReleased(event);
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        boolean isCtrlDown = event.hasControlDown();

        if (event.isEscape()) {
            manager.setDesignModeActive(false);
            this.onClose();
            return true;
        }
        if (event.key() == 76) { // 'L' Key - Toggle Lock
            manager.toggleLockSelected();
            return true;
        }
        if (isCtrlDown && event.key() == 90) { // Ctrl+Z - Undo
            manager.undo();
            return true;
        }
        if (isCtrlDown && event.key() == 89) { // Ctrl+Y - Redo
            manager.redo();
            return true;
        }
        if (isCtrlDown && event.key() == 67) { // Ctrl+C - Copy Style
            manager.copyStyle();
            return true;
        }
        if (isCtrlDown && event.key() == 86) { // Ctrl+V - Paste Style
            manager.pasteStyle();
            return true;
        }
        return super.keyPressed(event);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
