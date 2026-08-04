package com.nexui.ui.components;

import com.nexui.engine.RenderPipeline;
import com.nexui.model.ColorRGBA;
import com.nexui.model.ComponentStyle;
import com.nexui.model.Rect2i;
import com.nexui.model.Theme;
import net.minecraft.client.gui.DrawContext;

/**
 * Preview card widget for theme selection grid.
 */
public class ThemeCardWidget {

    public static void renderThemeCard(DrawContext context, Theme theme, Rect2i bounds, boolean isSelected) {
        if (theme == null || bounds == null) return;

        ComponentStyle cardStyle = new ComponentStyle();
        cardStyle.setBackgroundColor(theme.getBackgroundColor());
        cardStyle.setBorderColor(isSelected ? ColorRGBA.ACCENT_CYAN : theme.getPrimaryColor());
        cardStyle.setBorderWidth(isSelected ? 2 : 1);
        cardStyle.setBorderRadius(theme.getDefaultRadius());

        RenderPipeline.renderStyledPanel(context, bounds, cardStyle);

        int x = bounds.x();
        int y = bounds.y();

        context.drawText(context.getClient().textRenderer, theme.getName(), x + 10, y + 10, theme.getTextColor().toARGB(), false);
        context.drawText(context.getClient().textRenderer, theme.getDescription(), x + 10, y + 26, 0xAAFFFFFF, false);

        // Preview Swatches
        context.fill(x + 10, y + 42, x + 30, y + 54, theme.getPrimaryColor().toARGB());
        context.fill(x + 35, y + 42, x + 55, y + 54, theme.getSecondaryColor().toARGB());
        context.fill(x + 60, y + 42, x + 80, y + 54, theme.getAccentColor().toARGB());
    }
}
