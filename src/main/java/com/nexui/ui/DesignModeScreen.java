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
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.List;

/**
 * Interactive Figma-style Design Mode screen allowing real-time UI element drag, drop, resize, styling, alignment guides, and toolbar controls.
 */
public class DesignModeScreen extends Screen {
    private final DesignModeManager manager = DesignModeManager.getInstance();
    private boolean isDragging = false;
    private int dragStartX = 0;
    private int dragStartY = 0;
    private List<AlignmentGuideEngine.AlignmentGuide> currentGuides = List.of();

    public DesignModeScreen() {
        super(Text.literal("NexUI Design Mode Studio"));
    }

    @Override
    protected void init() {
        super.init();
        manager.setDesignModeActive(true);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        LayoutProfile activeProfile = ProfileRegistry.getInstance().getActiveProfile();

        // Render Canvas Grid if enabled
        if (activeProfile.isGridSnapEnabled()) {
            renderGrid(context, activeProfile.getGridSize());
        }

        // Render all UI components
        for (UIComponent component : activeProfile.getComponents().values()) {
            if (!component.isVisible()) continue;
            Rect2i bounds = component.getCurrentBounds();

            // Render component background panel
            RenderPipeline.renderStyledPanel(context, bounds, component.getStyle());

            // Label for editing identification
            context.drawText(context.getClient().textRenderer, component.getName(), bounds.x() + 4, bounds.y() + 4, ColorRGBA.WHITE.toARGB(), false);
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

    private void renderGrid(DrawContext context, int gridSize) {
        int gridColor = new ColorRGBA(255, 255, 255, 15).toARGB();
        for (int x = 0; x < width; x += gridSize * 2) {
            context.fill(x, 0, x + 1, height, gridColor);
        }
        for (int y = 0; y < height; y += gridSize * 2) {
            context.fill(0, y, width, y + 1, gridColor);
        }
    }

    private void renderToolbar(DrawContext context) {
        ComponentStyle barStyle = new ComponentStyle();
        barStyle.setBackgroundColor(new ColorRGBA(18, 18, 24, 230));
        barStyle.setBorderColor(ColorRGBA.ACCENT_BLUE);
        barStyle.setBorderWidth(1);

        Rect2i barBounds = new Rect2i(10, 10, width - 240, 36);
        RenderPipeline.renderStyledPanel(context, barBounds, barStyle);

        String title = "NexUI Design Mode Studio  |  Profile: " + ProfileRegistry.getInstance().getActiveProfile().getName();
        context.drawText(context.getClient().textRenderer, title, 20, 24, ColorRGBA.ACCENT_CYAN.toARGB(), false);

        String actions = "[ESC] Save & Exit  |  [L] Lock  |  [Ctrl+Z] Undo  |  [Ctrl+Y] Redo  |  [Ctrl+C] Copy Style";
        context.drawText(context.getClient().textRenderer, actions, width - 650, 24, ColorRGBA.WHITE.toARGB(), false);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int mx = (int) mouseX;
        int my = (int) mouseY;

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
            boolean isShiftDown = Screen.hasShiftDown();
            manager.selectComponent(clickedComp.getId(), isShiftDown);
            isDragging = true;
            dragStartX = mx;
            dragStartY = my;
            return true;
        } else {
            manager.clearSelection();
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
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
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        isDragging = false;
        currentGuides = List.of();
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 256) { // ESC Key
            manager.setDesignModeActive(false);
            this.close();
            return true;
        }
        if (keyCode == 76) { // 'L' Key - Toggle Lock
            manager.toggleLockSelected();
            return true;
        }
        if (Screen.hasControlDown() && keyCode == 90) { // Ctrl+Z - Undo
            manager.undo();
            return true;
        }
        if (Screen.hasControlDown() && keyCode == 89) { // Ctrl+Y - Redo
            manager.redo();
            return true;
        }
        if (Screen.hasControlDown() && keyCode == 67) { // Ctrl+C - Copy Style
            manager.copyStyle();
            return true;
        }
        if (Screen.hasControlDown() && keyCode == 86) { // Ctrl+V - Paste Style
            manager.pasteStyle();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
