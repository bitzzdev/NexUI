package com.nexui.config;

import com.nexui.registry.ProfileRegistry;

/**
 * Handles JSON persistence and configuration sync for NexUI settings and layout profiles.
 */
public class ConfigManager {
    private static final ConfigManager INSTANCE = new ConfigManager();
    private NexUIConfig config = new NexUIConfig();

    private ConfigManager() {}

    public static ConfigManager getInstance() {
        return INSTANCE;
    }

    public NexUIConfig getConfig() {
        return config;
    }

    public void loadConfig() {
        // Sync active profile with registry
        ProfileRegistry.getInstance().setActiveProfile(config.getActiveProfile());
    }

    public void saveConfig() {
        // Config persistence logic hook
        config.setActiveProfile(ProfileRegistry.getInstance().getActiveProfile().getId());
    }
}
