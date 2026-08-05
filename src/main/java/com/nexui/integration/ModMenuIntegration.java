package com.nexui.integration;

import com.nexui.ui.ThemeEditorScreen;
import net.minecraft.client.gui.screens.Screen;

/**
 * Native configuration screen factory integration for NexUI.
 */
public class ModMenuIntegration {

    public static Screen createConfigScreen(Screen parent) {
        return new ThemeEditorScreen();
    }
}
