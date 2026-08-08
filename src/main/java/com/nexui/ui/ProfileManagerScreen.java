package com.nexui.ui;

import com.nexui.config.ConfigManager;
import com.nexui.engine.RenderPipeline;
import com.nexui.model.ColorRGBA;
import com.nexui.model.ComponentStyle;
import com.nexui.model.LayoutProfile;
import com.nexui.model.Rect2i;
import com.nexui.registry.ProfileRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;

import java.util.List;

/**
 * Interface Layout Profile Manager Screen (Survival, PvP, Building, Streaming, Speedrunning, Accessibility).
 */
public class ProfileManagerScreen extends Screen {

    public ProfileManagerScreen() {
        super(Component.literal("NexUI Profile Manager"));
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta) {
        super.extractRenderState(context, mouseX, mouseY, delta);

        // Glass background panel
        ComponentStyle bgStyle = new ComponentStyle();
        bgStyle.setBackgroundColor(new ColorRGBA(12, 12, 18, 230));
        bgStyle.setBorderColor(ColorRGBA.ACCENT_CYAN);
        bgStyle.setBorderWidth(1);

        RenderPipeline.renderStyledPanel(context, new Rect2i(30, 30, width - 60, height - 60), bgStyle);

        // Header
        context.text(this.font, "NexUI Interface Layout Profiles", 50, 48, ColorRGBA.ACCENT_BLUE.toARGB(), false);
        context.text(this.font, "Switch profiles instantly for different playstyles or export/import JSON presets.", 50, 64, 0xAAFFFFFF, false);

        // Render Profiles List
        List<LayoutProfile> profiles = ProfileRegistry.getInstance().getAllProfiles();
        LayoutProfile active = ProfileRegistry.getInstance().getActiveProfile();

        int cardW = width - 120;
        int cardH = 46;
        int startX = 60;
        int startY = 90;
        int spacingY = 10;

        for (int i = 0; i < profiles.size(); i++) {
            LayoutProfile profile = profiles.get(i);
            int y = startY + i * (cardH + spacingY);

            boolean isActive = profile.getId().equals(active.getId());
            ComponentStyle cardStyle = new ComponentStyle();
            cardStyle.setBackgroundColor(isActive ? new ColorRGBA(30, 41, 59, 240) : new ColorRGBA(20, 20, 28, 200));
            cardStyle.setBorderColor(isActive ? ColorRGBA.ACCENT_CYAN : new ColorRGBA(99, 102, 241, 100));
            cardStyle.setBorderWidth(isActive ? 2 : 1);

            Rect2i bounds = new Rect2i(startX, y, cardW, cardH);
            RenderPipeline.renderStyledPanel(context, bounds, cardStyle);

            context.text(this.font, profile.getName() + (isActive ? " [ACTIVE]" : ""), startX + 16, y + 10, isActive ? ColorRGBA.ACCENT_CYAN.toARGB() : ColorRGBA.WHITE.toARGB(), false);
            context.text(this.font, profile.getDescription(), startX + 16, y + 26, 0xAAFFFFFF, false);
        }
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClicked) {
        int mx = (int) event.x();
        int my = (int) event.y();

        List<LayoutProfile> profiles = ProfileRegistry.getInstance().getAllProfiles();
        int cardW = width - 120;
        int cardH = 46;
        int startX = 60;
        int startY = 90;
        int spacingY = 10;

        for (int i = 0; i < profiles.size(); i++) {
            LayoutProfile profile = profiles.get(i);
            int y = startY + i * (cardH + spacingY);
            Rect2i bounds = new Rect2i(startX, y, cardW, cardH);
            if (bounds.contains(mx, my)) {
                ProfileRegistry.getInstance().setActiveProfile(profile.getId());
                ConfigManager.getInstance().saveConfig();
                return true;
            }
        }

        return super.mouseClicked(event, doubleClicked);
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        if (event.isEscape()) {
            Minecraft.getInstance().gui.setScreen(new DesignModeScreen());
            return true;
        }
        return super.keyPressed(event);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
