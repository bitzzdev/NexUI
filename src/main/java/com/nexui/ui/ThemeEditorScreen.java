package com.nexui.ui;

import com.nexui.engine.RenderPipeline;
import com.nexui.model.ColorRGBA;
import com.nexui.model.ComponentStyle;
import com.nexui.model.Rect2i;
import com.nexui.model.Theme;
import com.nexui.registry.ProfileRegistry;
import com.nexui.registry.ThemeRegistry;
import com.nexui.ui.components.ThemeCardWidget;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;

import java.util.List;

/**
 * Visual Theme Editor & Theme Browser Screen for selecting, creating, exporting, and customizing themes.
 */
public class ThemeEditorScreen extends Screen {

    public ThemeEditorScreen() {
        super(Component.literal("NexUI Theme Studio"));
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta) {
        this.extractBackground(context, mouseX, mouseY, delta);
        super.extractRenderState(context, mouseX, mouseY, delta);

        // Background Glass Panel
        ComponentStyle bgStyle = new ComponentStyle();
        bgStyle.setBackgroundColor(new ColorRGBA(12, 12, 18, 220));
        bgStyle.setBorderColor(ColorRGBA.ACCENT_BLUE);
        bgStyle.setBorderWidth(1);

        RenderPipeline.renderStyledPanel(context, new Rect2i(20, 20, width - 40, height - 40), bgStyle);

        // Header Title
        context.text(this.font, "NexUI Theme Browser & Customizer", 40, 36, ColorRGBA.ACCENT_CYAN.toARGB(), false);
        context.text(this.font, "Click a theme preset to apply it globally across all active interface elements.", 40, 52, 0xAAFFFFFF, false);

        // Render Theme Grid
        List<Theme> themes = ThemeRegistry.getInstance().getAllThemes();
        String currentThemeId = ProfileRegistry.getInstance().getActiveProfile().getThemeId();

        int cardW = 200;
        int cardH = 70;
        int startX = 40;
        int startY = 80;
        int spacingX = 16;
        int spacingY = 16;

        int cols = Math.max(1, (width - 100) / (cardW + spacingX));

        for (int i = 0; i < themes.size(); i++) {
            Theme theme = themes.get(i);
            int col = i % cols;
            int row = i / cols;
            int x = startX + col * (cardW + spacingX);
            int y = startY + row * (cardH + spacingY);

            Rect2i cardBounds = new Rect2i(x, y, cardW, cardH);
            boolean isSelected = theme.getId().equals(currentThemeId);

            ThemeCardWidget.renderThemeCard(context, theme, cardBounds, isSelected);
        }
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClicked) {
        int mx = (int) event.x();
        int my = (int) event.y();

        List<Theme> themes = ThemeRegistry.getInstance().getAllThemes();
        int cardW = 200;
        int cardH = 70;
        int startX = 40;
        int startY = 80;
        int spacingX = 16;
        int spacingY = 16;
        int cols = Math.max(1, (width - 100) / (cardW + spacingX));

        for (int i = 0; i < themes.size(); i++) {
            Theme theme = themes.get(i);
            int col = i % cols;
            int row = i / cols;
            int x = startX + col * (cardW + spacingX);
            int y = startY + row * (cardH + spacingY);

            Rect2i cardBounds = new Rect2i(x, y, cardW, cardH);
            if (cardBounds.contains(mx, my)) {
                ProfileRegistry.getInstance().getActiveProfile().setThemeId(theme.getId());
                return true;
            }
        }

        return super.mouseClicked(event, doubleClicked);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
