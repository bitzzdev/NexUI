package com.nexui.integration;

import com.nexui.model.LayoutProfile;
import com.nexui.model.Rect2i;
import com.nexui.model.UIComponent;
import com.nexui.registry.ProfileRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import org.joml.Matrix3x2fStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Wraps a vanilla HUD element and repositions (and optionally hides) it based on
 * the matching NexUI component. Only the delta between the component's current and
 * default bounds is applied, so untouched components render exactly like vanilla.
 */
public class RelocatedHudElement implements HudElement {
    private static final Logger LOGGER = LoggerFactory.getLogger("NexUI-HUD");
    private static boolean logged = false;

    private final String componentId;
    private final HudElement delegate;

    public RelocatedHudElement(String componentId, HudElement delegate) {
        this.componentId = componentId;
        this.delegate = delegate;
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor context, DeltaTracker deltaTracker) {
        LayoutProfile profile = ProfileRegistry.getInstance().getActiveProfile();
        UIComponent component = profile == null ? null : profile.getComponent(componentId);

        // Hiding a component now hides the real vanilla element.
        if (component != null && !component.isVisible()) {
            if (!logged) {
                logged = true;
                LOGGER.info("NexUI: hiding vanilla element for component '{}' (visible=false)", componentId);
            }
            return;
        }

        int dx = 0;
        int dy = 0;
        if (component != null) {
            Rect2i def = component.getDefaultBounds();
            Rect2i cur = component.getCurrentBounds();
            dx = cur.x() - def.x();
            dy = cur.y() - def.y();
        }

        if (dx == 0 && dy == 0) {
            if (!logged) {
                logged = true;
                LOGGER.info("NexUI: vanilla element for component '{}' rendering at default (delta 0, visible={})", componentId, component == null ? "?" : component.isVisible());
            }
            delegate.extractRenderState(context, deltaTracker);
            return;
        }

        if (!logged) {
            logged = true;
            LOGGER.info("NexUI: relocating vanilla element for component '{}' by ({}, {})", componentId, dx, dy);
        }

        Matrix3x2fStack pose = context.pose();
        pose.pushMatrix();
        pose.translateLocal(dx, dy);
        try {
            delegate.extractRenderState(context, deltaTracker);
        } finally {
            pose.popMatrix();
        }
    }
}
