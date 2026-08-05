package com.nexui;

import com.nexui.config.ConfigManager;
import com.nexui.engine.DesignModeManager;
import com.nexui.integration.ThirdPartyAdapters;
import com.nexui.ui.DesignModeScreen;
import com.nexui.ui.HudOverlayRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main Client Mod Initializer for NexUI - Modern Minecraft Interface Studio.
 */
public class NexUIClient implements ClientModInitializer {
    public static final String MOD_ID = "nexui";
    public static final Logger LOGGER = LoggerFactory.getLogger("NexUI");

    @Override
    public void onInitializeClient() {
        LOGGER.info("=================================================");
        LOGGER.info("  Initializing NexUI Studio v1.0.0 (Minecraft 26.2)");
        LOGGER.info("=================================================");

        // 1. Load Configurations
        ConfigManager.getInstance().loadConfig();

        // 2. Register Client Tick Event Listener
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client != null && client.player != null) {
                // Keybind handling hook
            }
        });

        // 3. Register HUD Overlay Renderer Hook
        HudRenderCallback.EVENT.register(HudOverlayRenderer::onHudRender);

        // 4. Initialize Mod Compatibility Hooks
        ThirdPartyAdapters.initializeCompatibilityHooks();

        LOGGER.info("[NexUI] Initialization complete.");
    }
}
