package com.nexui.ui;

import com.nexui.engine.AnimationController;
import com.nexui.engine.DesignModeManager;
import com.nexui.engine.RenderPipeline;
import com.nexui.model.LayoutProfile;
import com.nexui.model.Rect2i;
import com.nexui.model.UIComponent;
import com.nexui.registry.ProfileRegistry;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;

/**
 * Custom Client HUD Overlay Renderer Hook.
 */
public class HudOverlayRenderer {

    public static void onHudRender(GuiGraphics context, DeltaTracker tickCounter) {
        // Do not render HUD duplicate elements during active Design Mode
        if (DesignModeManager.getInstance().isDesignModeActive()) {
            return;
        }

        LayoutProfile activeProfile = ProfileRegistry.getInstance().getActiveProfile();
        if (activeProfile == null) return;

        float delta = tickCounter.getGameTimeDeltaPartialTick(false);

        for (UIComponent component : activeProfile.getComponents().values()) {
            if (!component.isVisible()) continue;

            Rect2i currentBounds = component.getCurrentBounds();
            // Calculate animated properties
            Rect2i animatedBounds = AnimationController.getAnimatedBounds(component, currentBounds, delta);

            // Render component custom background glass & border style
            RenderPipeline.renderStyledPanel(context, animatedBounds, component.getStyle());
        }
    }
}
