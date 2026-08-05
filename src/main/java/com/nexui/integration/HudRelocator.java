package com.nexui.integration;

import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.resources.Identifier;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Hooks every NexUI HUD component id onto the corresponding vanilla HUD element
 * so that moving/resizing/hiding a component actually affects the real element.
 */
public final class HudRelocator {
    private static final Map<String, Identifier> COMPONENT_TO_LAYER = new LinkedHashMap<>();

    static {
        COMPONENT_TO_LAYER.put("hotbar", VanillaHudElements.HOTBAR);
        COMPONENT_TO_LAYER.put("hearts", VanillaHudElements.HEALTH_BAR);
        COMPONENT_TO_LAYER.put("hunger", VanillaHudElements.FOOD_BAR);
        COMPONENT_TO_LAYER.put("armor", VanillaHudElements.ARMOR_BAR);
        COMPONENT_TO_LAYER.put("xp_bar", VanillaHudElements.INFO_BAR);
        COMPONENT_TO_LAYER.put("xp_level", VanillaHudElements.EXPERIENCE_LEVEL);
        COMPONENT_TO_LAYER.put("crosshair", VanillaHudElements.CROSSHAIR);
        COMPONENT_TO_LAYER.put("air_bar", VanillaHudElements.AIR_BAR);
        COMPONENT_TO_LAYER.put("mount_hud", VanillaHudElements.MOUNT_HEALTH);
        COMPONENT_TO_LAYER.put("action_bar", VanillaHudElements.OVERLAY_MESSAGE);
        COMPONENT_TO_LAYER.put("chat", VanillaHudElements.CHAT);
        COMPONENT_TO_LAYER.put("scoreboard", VanillaHudElements.SCOREBOARD);
        COMPONENT_TO_LAYER.put("titles", VanillaHudElements.TITLE_AND_SUBTITLE);
        COMPONENT_TO_LAYER.put("boss_bars", VanillaHudElements.BOSS_BAR);
    }

    private HudRelocator() {
    }

    /**
     * Replaces each mapped vanilla HUD element with a relocated wrapper. The wrapper
     * resolves the component lazily from the active profile, so registration order
     * relative to profile creation does not matter.
     */
    public static void registerRelocations() {
        COMPONENT_TO_LAYER.forEach((componentId, layerId) ->
            HudElementRegistry.replaceElement(layerId, vanilla -> new RelocatedHudElement(componentId, vanilla))
        );
    }
}
