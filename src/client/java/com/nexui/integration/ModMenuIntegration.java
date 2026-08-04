package com.nexui.integration;

import com.nexui.ui.DesignModeScreen;
import net.minecraft.client.gui.screen.Screen;

/**
 * Mod Menu & Cloth Config integration bridge.
 */
public class ModMenuIntegration {

    public static Screen createConfigScreen(Screen parent) {
        return new DesignModeScreen();
    }
}
