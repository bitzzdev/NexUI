package com.nexui.integration;

import com.nexui.ui.DesignModeScreen;
import net.minecraft.client.gui.screen.Screen;

/**
 * Mod Menu integration config factory hook.
 */
public class ModMenuIntegration {

    public static Screen createConfigScreen(Screen parent) {
        return new DesignModeScreen();
    }
}
