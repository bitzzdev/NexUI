package com.nexui.ui;

import com.nexui.engine.AnimationController;
import com.nexui.engine.DesignModeManager;
import com.nexui.engine.RenderPipeline;
import com.nexui.model.LayoutProfile;
import com.nexui.model.Rect2i;
import com.nexui.model.UIComponent;
import com.nexui.registry.ProfileRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphicsExtractor;

/**
 * Custom Client HUD Overlay Renderer Hook.
 *
 * In Minecraft 26.2 the HUD rendering pipeline was rewritten around render-state
 * extraction. HUD extensions now implement {@link HudElement}, which exposes a
 * single {@code extractRenderState(GuiGraphicsExtractor, DeltaTracker)} method.
 */
public class HudOverlayRenderer implements HudElement {
    public static final HudOverlayRenderer INSTANCE = new HudOverlayRenderer();

    private HudOverlayRenderer() {
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor context, DeltaTracker tickCounter) {
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
