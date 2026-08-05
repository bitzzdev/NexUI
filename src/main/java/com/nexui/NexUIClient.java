package com.nexui;

import com.nexui.config.ConfigManager;
import com.nexui.registry.ProfileRegistry;
import com.nexui.registry.ThemeRegistry;
import com.nexui.ui.DesignModeScreen;
import com.nexui.ui.HudOverlayRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.option.KeyBinding;
import org.lwjgl.glfw.GLFW;

public class NexUIClient implements ClientModInitializer {
    public static final String MOD_ID = "nexui";
    private static KeyBinding openDesignModeKey;

    @Override
    public void onInitializeClient() {
        ConfigManager.getInstance().loadConfig();
        ThemeRegistry.getInstance().registerDefaultThemes();
        ProfileRegistry.getInstance().registerDefaultProfiles();

        openDesignModeKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.nexui.open_design_mode",
            GLFW.GLFW_KEY_RIGHT_SHIFT,
            "category.nexui.general"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openDesignModeKey.wasPressed()) {
                if (client.currentScreen == null) {
                    client.setScreen(new DesignModeScreen());
                }
            }
        });

        HudRenderCallback.EVENT.register(HudOverlayRenderer::onHudRender);
    }
}
