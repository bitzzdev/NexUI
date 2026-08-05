package com.nexui.ui.components;

import com.nexui.engine.RenderPipeline;
import com.nexui.model.ColorRGBA;
import com.nexui.model.ComponentStyle;
import com.nexui.model.Rect2i;
import com.nexui.model.UIComponent;
import net.minecraft.client.gui.GuiGraphics;

/**
 * Figma-inspired right sidebar property inspector panel.
 */
public class PropertyInspectorWidget {
    private static final int PANEL_WIDTH = 220;

    public static void renderInspector(GuiGraphics context, UIComponent component, int screenWidth, int screenHeight) {
        if (component == null) return;
        int x = screenWidth - PANEL_WIDTH - 10;
        int y = 10;
        int height = screenHeight - 20;

        ComponentStyle inspectorStyle = new ComponentStyle();
        inspectorStyle.setBackgroundColor(new ColorRGBA(18, 18, 24, 230));
        inspectorStyle.setBorderColor(ColorRGBA.ACCENT_BLUE);
        inspectorStyle.setBorderWidth(1);
        inspectorStyle.setShadowRadius(8);

        Rect2i panelBounds = new Rect2i(x, y, PANEL_WIDTH, height);
        RenderPipeline.renderStyledPanel(context, panelBounds, inspectorStyle);

        // Header
        context.drawString(context.getClient().font, "Inspector: " + component.getName(), x + 12, y + 14, ColorRGBA.ACCENT_CYAN.toARGB(), false);
        context.drawString(context.getClient().font, "Category: " + component.getCategory().getLabel(), x + 12, y + 28, ColorRGBA.WHITE.toARGB(), false);

        // Bounds Information
        Rect2i b = component.getCurrentBounds();
        context.drawString(context.getClient().font, String.format("X: %d  |  Y: %d", b.x(), b.y()), x + 12, y + 48, ColorRGBA.WHITE.toARGB(), false);
        context.drawString(context.getClient().font, String.format("W: %d  |  H: %d", b.width(), b.height()), x + 12, y + 62, ColorRGBA.WHITE.toARGB(), false);

        // Styling Details
        ComponentStyle s = component.getStyle();
        context.drawString(context.getClient().font, "Border Radius: " + s.getBorderRadius() + "px", x + 12, y + 86, ColorRGBA.WHITE.toARGB(), false);
        context.drawString(context.getClient().font, "Border Width: " + s.getBorderWidth() + "px", x + 12, y + 100, ColorRGBA.WHITE.toARGB(), false);
        context.drawString(context.getClient().font, "Blur Radius: " + s.getBlurRadius() + "px", x + 12, y + 114, ColorRGBA.WHITE.toARGB(), false);
        context.drawString(context.getClient().font, "Opacity: " + (int)(s.getOpacity() * 100) + "%", x + 12, y + 128, ColorRGBA.WHITE.toARGB(), false);
        context.drawString(context.getClient().font, "Animation: " + s.getAnimationType().getDisplayName(), x + 12, y + 142, ColorRGBA.ACCENT_BLUE.toARGB(), false);
        context.drawString(context.getClient().font, "Status: " + (component.isLocked() ? "LOCKED" : "EDITABLE"), x + 12, y + 160, component.isLocked() ? 0xFFFF4444 : 0xFF44FF44, false);
    }
}
