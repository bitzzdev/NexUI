package com.nexui.integration;

import com.nexui.ui.DesignModeScreen;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

/**
 * Mod Menu integration: adds a "Configure..." button on the NexUI entry in the
 * mod list that opens the NexUI Design Studio directly.
 */
public class ModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> new DesignModeScreen();
    }
}
