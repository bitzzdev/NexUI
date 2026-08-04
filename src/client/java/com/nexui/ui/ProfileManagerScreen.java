package com.nexui.ui;

import com.nexui.engine.RenderPipeline;
import com.nexui.model.ColorRGBA;
import com.nexui.model.ComponentStyle;
import com.nexui.model.LayoutProfile;
import com.nexui.model.Rect2i;
import com.nexui.registry.ProfileRegistry;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.List;

/**
 * Interface Layout Profile Manager Screen (Survival, PvP, Building, Streaming, Speedrunning, Accessibility).
 */
public class ProfileManagerScreen extends Screen {

    public ProfileManagerScreen() {
        super(Text.literal("NexUI Profile Manager"));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        // Glass background panel
        ComponentStyle bgStyle = new ComponentStyle();
        bgStyle.setBackgroundColor(new ColorRGBA(12, 12, 18, 230));
        bgStyle.setBorderColor(ColorRGBA.ACCENT_CYAN);
        bgStyle.setBorderWidth(1);

        RenderPipeline.renderStyledPanel(context, new Rect2i(30, 30, width - 60, height - 60), bgStyle);

        // Header
        context.drawText(this.textRenderer, "NexUI Interface Layout Profiles", 50, 48, ColorRGBA.ACCENT_BLUE.toARGB(), false);
        context.drawText(this.textRenderer, "Switch profiles instantly for different playstyles or export/import JSON presets.", 50, 64, 0xAAFFFFFF, false);

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

            context.drawText(this.textRenderer, profile.getName() + (isActive ? " [ACTIVE]" : ""), startX + 16, y + 10, isActive ? ColorRGBA.ACCENT_CYAN.toARGB() : ColorRGBA.WHITE.toARGB(), false);
            context.drawText(this.textRenderer, profile.getDescription(), startX + 16, y + 26, 0xAAFFFFFF, false);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int mx = (int) mouseX;
        int my = (int) mouseY;

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
                return true;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
