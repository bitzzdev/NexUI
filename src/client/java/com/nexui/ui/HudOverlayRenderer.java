package com.nexui.ui;

import com.nexui.api.NexUIWidget;
import com.nexui.engine.RenderPipeline;
import com.nexui.model.LayoutProfile;
import com.nexui.model.Rect2i;
import com.nexui.model.Theme;
import com.nexui.model.UIComponent;
import com.nexui.registry.ProfileRegistry;
import com.nexui.registry.ThemeRegistry;
import com.nexui.registry.WidgetRegistry;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;

/**
 * Modern HUD Overlay renderer hook responsible for drawing custom themed UI elements.
 */
public class HudOverlayRenderer {

    public static void onHudRender(DrawContext context, RenderTickCounter tickCounter) {
        LayoutProfile activeProfile = ProfileRegistry.getInstance().getActiveProfile();
        Theme activeTheme = ThemeRegistry.getInstance().getTheme(activeProfile.getThemeId());

        if (activeProfile == null || activeTheme == null) return;

        // Render third-party and customized registered HUD components
        for (UIComponent component : activeProfile.getComponents().values()) {
            if (!component.isVisible()) continue;

            NexUIWidget customWidget = WidgetRegistry.getInstance().getCustomWidget(component.getId());
            if (customWidget != null && customWidget.isAvailable()) {
                Rect2i b = component.getCurrentBounds();
                customWidget.render(b.x(), b.y(), b.width(), b.height(), component.getStyle(), tickCounter.getTickDelta(true));
            }
        }
    }
}
