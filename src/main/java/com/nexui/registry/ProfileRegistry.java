package com.nexui.registry;

import com.nexui.model.LayoutProfile;
import com.nexui.model.UIComponent;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Registry managing layout profiles (Survival, PvP, Building, Streaming, Speedrunning, Accessibility).
 */
public class ProfileRegistry {
    private static final ProfileRegistry INSTANCE = new ProfileRegistry();
    private final Map<String, LayoutProfile> profiles = new LinkedHashMap<>();
    private String activeProfileId = "survival";

    private ProfileRegistry() {
        registerDefaultProfiles();
    }

    public static ProfileRegistry getInstance() {
        return INSTANCE;
    }

    private void registerDefaultProfiles() {
        // Survival Profile
        LayoutProfile survival = createBaseProfile("survival", "Survival Default", "Standard balanced layout", "modern");
        profiles.put(survival.getId(), survival);

        // PvP Profile
        LayoutProfile pvp = createBaseProfile("pvp", "Competitive PvP", "Ultra-compact HUD with centralized health and status", "pvp");
        pvp.setGridSize(4);
        profiles.put(pvp.getId(), pvp);

        // Building Profile
        LayoutProfile building = createBaseProfile("building", "Architect & Building", "Expanded hotbar and clean center view", "minimal");
        profiles.put(building.getId(), building);

        // Streaming Profile
        LayoutProfile streamer = createBaseProfile("streaming", "Streamer Broadcast", "Visually distinct borders optimized for facecam overlays", "streamer");
        profiles.put(streamer.getId(), streamer);

        // Speedrunning Profile
        LayoutProfile speedrun = createBaseProfile("speedrunning", "Speedrun Timer & Stats", "High visibility action overlays and timer area", "amoled");
        profiles.put(speedrun.getId(), speedrun);

        // Accessibility Profile
        LayoutProfile accessibility = createBaseProfile("accessibility", "High Contrast & Scale", "Enlarged fonts and high contrast presets", "rpg");
        for (UIComponent comp : accessibility.getComponents().values()) {
            comp.setScale(1.3f);
            comp.getStyle().setBorderWidth(2);
            comp.getStyle().setFontSize(1.4f);
        }
        profiles.put(accessibility.getId(), accessibility);
    }

    private LayoutProfile createBaseProfile(String id, String name, String desc, String themeId) {
        LayoutProfile profile = new LayoutProfile(id, name, desc, themeId);
        Map<String, UIComponent> defaults = WidgetRegistry.getInstance().getDefaultComponents();
        for (UIComponent comp : defaults.values()) {
            profile.addComponent(comp.copy());
        }
        return profile;
    }

    public LayoutProfile getActiveProfile() {
        return profiles.getOrDefault(activeProfileId, profiles.get("survival"));
    }

    public void setActiveProfile(String id) {
        if (profiles.containsKey(id)) {
            this.activeProfileId = id;
        }
    }

    /**
     * Moves the active profile to the next/previous entry in registration order,
     * wrapping around. Positive direction goes forward, negative backward.
     */
    public void cycleActiveProfile(int direction) {
        List<String> ids = new ArrayList<>(profiles.keySet());
        if (ids.isEmpty()) {
            return;
        }
        int index = ids.indexOf(activeProfileId);
        if (index < 0) {
            index = 0;
        }
        int next = ((index + direction) % ids.size() + ids.size()) % ids.size();
        this.activeProfileId = ids.get(next);
    }

    public void registerProfile(LayoutProfile profile) {
        if (profile != null) {
            profiles.put(profile.getId(), profile);
        }
    }

    public List<LayoutProfile> getAllProfiles() {
        return new ArrayList<>(profiles.values());
    }

    public LayoutProfile createCustomProfile(String name, String description) {
        String id = "custom_" + System.currentTimeMillis();
        LayoutProfile base = getActiveProfile();
        LayoutProfile newProf = base.copy(id, name);
        newProf.setDescription(description);
        registerProfile(newProf);
        return newProf;
    }
}
