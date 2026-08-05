package com.nexui.ui.components;

import com.nexui.engine.RenderPipeline;
import com.nexui.model.ColorRGBA;
import com.nexui.model.ComponentStyle;
import com.nexui.model.Rect2i;
import com.nexui.model.UIComponent;
import net.minecraft.client.gui.GuiGraphicsExtractor;

/**
 * Figma-inspired right sidebar property inspector panel.
 */
public class PropertyInspectorWidget {
    private static final int PANEL_WIDTH = 220;

    public static void renderInspector(GuiGraphicsExtractor context, UIComponent component, int screenWidth, int screenHeight) {
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
    }
}
