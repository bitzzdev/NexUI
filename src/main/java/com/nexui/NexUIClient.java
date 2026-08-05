package com.nexui;

import com.mojang.blaze3d.platform.InputConstants;
import com.nexui.config.ConfigManager;
import com.nexui.registry.ProfileRegistry;
import com.nexui.registry.ThemeRegistry;
import com.nexui.ui.DesignModeScreen;
import com.nexui.ui.HudOverlayRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

public class NexUIClient implements ClientModInitializer {
    public static final String MOD_ID = "nexui";
    private static KeyMapping openDesignModeKey;

    @Override
    public void onInitializeClient() {
        ConfigManager.getInstance().loadConfig();
        ThemeRegistry.getInstance();
        ProfileRegistry.getInstance();

        // KeyMapping now takes a KeyMapping.Category instead of a raw translation key.
        // Fabric's old KeyBindingHelper is gone; use KeyMappingHelper instead.
        openDesignModeKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
            "key.nexui.open_design_mode",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_RIGHT_SHIFT,
            KeyMapping.Category.MISC
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openDesignModeKey.consumeClick()) {
                // Screen management moved from Minecraft to Minecraft#gui (net.minecraft.client.gui.Gui)
                if (client.gui.screen() == null) {
                    client.gui.setScreen(new DesignModeScreen());
                }
            }
        });

        // HUD hooks live under net.fabricmc.fabric.api.client.rendering.v1.hud in Fabric API 26.2
        HudElementRegistry.attachElementAfter(
            VanillaHudElements.MISC_OVERLAYS,
            Identifier.fromNamespaceAndPath(MOD_ID, "hud_overlay"),
            HudOverlayRenderer.INSTANCE
        );
    }
}
