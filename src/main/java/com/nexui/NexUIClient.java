package com.nexui;

import com.mojang.blaze3d.platform.InputConstants;
import com.nexui.config.ConfigManager;
import com.nexui.integration.HudRelocator;
import com.nexui.model.LayoutProfile;
import com.nexui.registry.ProfileRegistry;
import com.nexui.registry.ThemeRegistry;
import com.nexui.ui.DesignModeScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class NexUIClient implements ClientModInitializer {
    public static final String MOD_ID = "nexui";
    private static KeyMapping openDesignModeKey;
    private static KeyMapping resetLayoutKey;

    @Override
    public void onInitializeClient() {
        ConfigManager.getInstance().loadConfig();
        ThemeRegistry.getInstance();
        ProfileRegistry.getInstance();

        // Register a dedicated "NexUI Studio" category so the keybind is clearly
        // visible in Options -> Controls -> Keyboard/Mouse (instead of being buried
        // under the vanilla "Miscellaneous" section).
        KeyMapping.Category nexuiCategory = KeyMapping.Category.register(
            Identifier.fromNamespaceAndPath(MOD_ID, "general")
        );

        // KeyMapping now takes a KeyMapping.Category instead of a raw translation key.
        // Fabric's old KeyBindingHelper is gone; use KeyMappingHelper instead.
        openDesignModeKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
            "key.nexui.open_design_mode",
            InputConstants.Type.KEYSYM,
            ConfigManager.getInstance().getConfig().getToggleHotkeyKeyCode(),
            nexuiCategory
        ));

        resetLayoutKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
            "key.nexui.reset_layout",
            InputConstants.Type.KEYSYM,
            ConfigManager.getInstance().getConfig().getResetHotkeyKeyCode(),
            nexuiCategory
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openDesignModeKey.consumeClick()) {
                // Screen management moved from Minecraft to Minecraft#gui (net.minecraft.client.gui.Gui)
                if (client.gui.screen() == null) {
                    client.gui.setScreen(new DesignModeScreen());
                }
            }
            while (resetLayoutKey.consumeClick()) {
                resetLayoutToVanilla();
            }
        });

        // Real HUD element relocation: replace each vanilla HUD element with a wrapper
        // that moves/hides it according to the active profile's component placement.
        HudRelocator.registerRelocations();
    }

    private void resetLayoutToVanilla() {
        LayoutProfile profile = ProfileRegistry.getInstance().getActiveProfile();
        if (profile == null) return;
        Minecraft client = Minecraft.getInstance();
        int w = client.getWindow().getGuiScaledWidth();
        int h = client.getWindow().getGuiScaledHeight();
        profile.resetToVanilla(w, h);
        ConfigManager.getInstance().saveConfig();
        if (client.player != null) {
            client.player.sendSystemMessage(Component.literal("NexUI: layout reset to vanilla"));
        }
    }
}
