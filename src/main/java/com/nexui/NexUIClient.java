package com.nexui;

import com.nexui.config.ConfigManager;
import com.nexui.engine.DesignModeManager;
import com.nexui.integration.ThirdPartyAdapters;
import com.nexui.ui.DesignModeScreen;
import com.nexui.ui.HudOverlayRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main Client Mod Initializer for NexUI - Modern Minecraft Interface Studio.
 */
public class NexUIClient implements ClientModInitializer {
    public static final String MOD_ID = "nexui";
    public static final Logger LOGGER = LoggerFactory.getLogger("NexUI");

    private static KeyBinding toggleDesignModeKey;

    @Override
    public void onInitializeClient() {
        LOGGER.info("=================================================");
        LOGGER.info("  Initializing NexUI Studio v1.0.0 (Minecraft 26.2)");
        LOGGER.info("=================================================");

        // 1. Load Configurations
        ConfigManager.getInstance().loadConfig();

        // 2. Register Hotkeys (Default: Right Shift)
        toggleDesignModeKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.nexui.toggle_design_mode",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_RIGHT_SHIFT,
            "category.nexui.title"
        ));

        // 3. Register Keybind Event Listener
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggleDesignModeKey.wasPressed()) {
                if (client.currentScreen == null) {
                    client.setScreen(new DesignModeScreen());
                } else if (client.currentScreen instanceof DesignModeScreen) {
                    DesignModeManager.getInstance().setDesignModeActive(false);
                    client.currentScreen.close();
                }
            }
        });

        // 4. Register HUD Overlay Renderer Hook
        HudRenderCallback.EVENT.register(HudOverlayRenderer::onHudRender);

        // 5. Initialize Mod Compatibility Hooks
        ThirdPartyAdapters.initializeCompatibilityHooks();

        LOGGER.info("[NexUI] Initialization complete. Press RIGHT SHIFT in-game to launch Design Mode.");
    }
}
