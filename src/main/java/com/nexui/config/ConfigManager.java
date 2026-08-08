package com.nexui.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nexui.model.LayoutProfile;
import com.nexui.model.UIComponent;
import com.nexui.registry.ProfileRegistry;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Handles JSON persistence and configuration sync for NexUI settings and layout profiles.
 * The active profile's component placement is snapshotted to {@code config/nexui.json}
 * on every save and restored on load, so layouts survive game restarts.
 */
public class ConfigManager {
    private static final Logger LOGGER = LoggerFactory.getLogger("NexUI-Config");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String CONFIG_FILE_NAME = "nexui.json";

    private static final ConfigManager INSTANCE = new ConfigManager();
    private NexUIConfig config = new NexUIConfig();

    private ConfigManager() {}

    public static ConfigManager getInstance() {
        return INSTANCE;
    }

    public NexUIConfig getConfig() {
        return config;
    }

    private Path configPath() {
        return FabricLoader.getInstance().getConfigDir().resolve(CONFIG_FILE_NAME);
    }

    public void loadConfig() {
        Path path = configPath();
        if (Files.exists(path)) {
            try {
                NexUIConfig loaded = GSON.fromJson(Files.readString(path), NexUIConfig.class);
                if (loaded != null) {
                    config = loaded;
                }
            } catch (Exception e) {
                LOGGER.error("NexUI: failed to load config from {}", path, e);
            }
        }

        // Sync active profile with registry, then restore its saved component placement.
        ProfileRegistry.getInstance().setActiveProfile(config.getActiveProfile());
        applyLayout();
    }

    public void saveConfig() {
        // Snapshot the active profile's component placement before writing.
        config.setActiveProfile(ProfileRegistry.getInstance().getActiveProfile().getId());
        captureLayout();

        Path path = configPath();
        try {
            Files.createDirectories(path.getParent());
            Files.writeString(path, GSON.toJson(config));
        } catch (IOException e) {
            LOGGER.error("NexUI: failed to save config to {}", path, e);
        }
    }

    private void captureLayout() {
        config.getLayout().clear();
        LayoutProfile profile = ProfileRegistry.getInstance().getActiveProfile();
        if (profile == null) {
            return;
        }
        for (UIComponent component : profile.getComponents().values()) {
            config.getLayout().put(component.getId(), ComponentState.from(component));
        }
    }

    private void applyLayout() {
        LayoutProfile profile = ProfileRegistry.getInstance().getActiveProfile();
        if (profile == null) {
            return;
        }
        for (UIComponent component : profile.getComponents().values()) {
            ComponentState state = config.getLayout().get(component.getId());
            if (state != null) {
                state.applyTo(component);
            }
        }
    }
}
