package com.nexui.ui;

import com.nexui.engine.AlignmentGuideEngine;
import com.nexui.engine.DesignModeManager;
import com.nexui.engine.GridSnapEngine;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Interactive Figma-style Design Mode screen allowing real-time UI element drag, drop, resize, styling, alignment guides, and toolbar controls.
 */
public class DesignModeScreen extends Screen {
    private static final Logger LOGGER = LoggerFactory.getLogger("NexUI-Design");
    private static final ComponentStyle RELOCATOR_BOX_STYLE = createRelocatorStyle();
    private final DesignModeManager manager = DesignModeManager.getInstance();
    private boolean isDragging = false;
    private boolean dragOriginRecorded = false;
    private double dragStartMouseX;
    private double dragStartMouseY;
    private final Map<String, Rect2i> dragStartBounds = new HashMap<>();
    private List<AlignmentGuideEngine.AlignmentGuide> currentGuides = List.of();

    public DesignModeScreen() {
        super(Component.literal("NexUI Design Mode Studio"));
    }

    @Override
    protected void init() {
        super.init();
        manager.setDesignModeActive(true);
        dSnapshot("profile");
    }

    private static ComponentStyle createRelocatorStyle() {
        ComponentStyle style = new ComponentStyle();
        style.setBackgroundColor(new ColorRGBA(45, 52, 110, 170));
        style.setBorderColor(ColorRGBA.ACCENT_CYAN);
        style.setBorderWidth(2);
        return style;
    }

    private void dSnapshot(String tag) {
        LayoutProfile active = ProfileRegistry.getInstance().getActiveProfile();
        LOGGER.info("[{}] design canvas {}x{} profile='{}' components={}",
            tag, width, height, active.getId(), active.getComponents().size());
        for (UIComponent comp : active.getComponents().values()) {
            LOGGER.info("  comp id='{}' name='{}' visible={} bounds={} category={}",
                comp.getId(), comp.getName(), comp.isVisible(),
                comp.getCurrentBounds(), comp.getCategory().name());
        }
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

        // Render all visible UI components (hidden ones are not shown on the canvas)
        for (UIComponent component : activeProfile.getComponents().values()) {
            if (!component.isVisible()) continue;

            Rect2i bounds = component.getCurrentBounds();

            // Render the relocator box with a fixed, clearly visible design style so
            // every element on the canvas is easy to spot regardless of the scene.
            RenderPipeline.renderStyledPanel(context, bounds, RELOCATOR_BOX_STYLE);

            // Label for editing identification
            context.text(this.font, component.getName(), bounds.x() + 4, bounds.y() + 4, ColorRGBA.WHITE.toARGB(), false);
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

    private Rect2i profilesButtonBounds() {
        return new Rect2i(width - 230, 17, 100, 38);
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

        String actions = "[ESC] Save & Exit  |  [L] Lock  |  [Ctrl+Z] Undo  |  [Ctrl+Y] Redo  |  [Ctrl+C] Copy Style  |  [[<] Cycle Profile";
        context.text(this.font, actions, 20, 38, ColorRGBA.WHITE.toARGB(), false);

        Rect2i buttonBounds = visibilityButtonBounds();
        ComponentStyle buttonStyle = new ComponentStyle();
        buttonStyle.setBackgroundColor(new ColorRGBA(99, 102, 241, 230));
        buttonStyle.setBorderColor(ColorRGBA.ACCENT_BLUE);
        buttonStyle.setBorderWidth(1);
        RenderPipeline.renderStyledPanel(context, buttonBounds, buttonStyle);
        context.text(this.font, "Visibility", buttonBounds.x() + (buttonBounds.width() - this.font.width("Visibility")) / 2, buttonBounds.y() + 14, ColorRGBA.WHITE.toARGB(), false);

        Rect2i profilesBounds = profilesButtonBounds();
        RenderPipeline.renderStyledPanel(context, profilesBounds, buttonStyle);
        context.text(this.font, "Profiles", profilesBounds.x() + (profilesBounds.width() - this.font.width("Profiles")) / 2, profilesBounds.y() + 14, ColorRGBA.WHITE.toARGB(), false);
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClicked) {
        int mx = (int) event.x();
        int my = (int) event.y();

        if (visibilityButtonBounds().contains(mx, my)) {
            Minecraft.getInstance().gui.setScreen(new ElementVisibilityScreen());
            return true;
        }
        if (profilesButtonBounds().contains(mx, my)) {
            Minecraft.getInstance().gui.setScreen(new ProfileManagerScreen());
            return true;
        }

        LayoutProfile activeProfile = ProfileRegistry.getInstance().getActiveProfile();
        UIComponent clickedComp = null;

        // Check component hit test, preferring the smallest box containing the
        // click so tightly packed elements (e.g. the hotbar cluster) can each be
        // grabbed individually instead of always hitting the largest overlay.
        int bestArea = Integer.MAX_VALUE;
        for (UIComponent comp : activeProfile.getComponents().values()) {
            if (comp.isVisible() && comp.getCurrentBounds().contains(mx, my)) {
                Rect2i b = comp.getCurrentBounds();
                int area = b.width() * b.height();
                if (area < bestArea) {
                    bestArea = area;
                    clickedComp = comp;
                }
            }
        }

        if (clickedComp != null) {
            manager.selectComponent(clickedComp.getId(), false);
            isDragging = true;
            LOGGER.info("NexUI-design: click picked component '{}' at ({}, {})", clickedComp.getId(), mx, my);
            return true;
        } else {
            manager.clearSelection();
        }

        return super.mouseClicked(event, doubleClicked);
    }

    @Override
    public boolean mouseDragged(MouseButtonEvent event, double deltaX, double deltaY) {
        if (isDragging && manager.getPrimarySelectedComponent() != null) {
            if (!dragOriginRecorded) {
                dragOriginRecorded = true;
                dragStartMouseX = event.x();
                dragStartMouseY = event.y();
                dragStartBounds.clear();
                for (UIComponent comp : manager.getSelectedComponents()) {
                    dragStartBounds.put(comp.getId(), comp.getCurrentBounds());
                }
                manager.beginSelectedComponentDrag();
            }

            LayoutProfile activeProfile = ProfileRegistry.getInstance().getActiveProfile();
            int offsetX = (int) (event.x() - dragStartMouseX);
            int offsetY = (int) (event.y() - dragStartMouseY);

            Map<String, Rect2i> targets = new HashMap<>();
            for (UIComponent comp : manager.getSelectedComponents()) {
                if (comp.isLocked()) continue;
                Rect2i start = dragStartBounds.get(comp.getId());
                if (start == null) continue;
                int tx = start.x() + offsetX;
                int ty = start.y() + offsetY;
                if (activeProfile.isGridSnapEnabled()) {
                    tx = GridSnapEngine.snapValue(tx, activeProfile.getGridSize());
                    ty = GridSnapEngine.snapValue(ty, activeProfile.getGridSize());
                }
                targets.put(comp.getId(), new Rect2i(tx, ty, start.width(), start.height()));
            }
            manager.moveSelectedComponentsTo(targets);
            if (!targets.isEmpty()) {
                LOGGER.info("NexUI-design: drag moved {} targets: {}", targets.size(), targets.keySet());
            }

            // Compute smart alignment guides
            UIComponent primary = manager.getPrimarySelectedComponent();
            if (activeProfile.isSmartGuidesEnabled() && primary != null) {
                currentGuides = AlignmentGuideEngine.computeGuides(
                    primary, activeProfile.getComponents().values(), width, height
                ).guides;
            }
            return true;
        }
        return super.mouseDragged(event, deltaX, deltaY);
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent event) {
        isDragging = false;
        dragOriginRecorded = false;
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
