package com.nexui.config;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Mod configuration parameters for NexUI studio.
 */
public class NexUIConfig {
    private String activeProfile = "survival";
    private String activeTheme = "modern";
    private int toggleHotkeyKeyCode = 344; // Right Shift
    private int resetHotkeyKeyCode = 85; // U
    private boolean gridSnap = true;
    private int defaultGridSize = 8;
    private boolean smartGuides = true;
    private boolean animations = true;
    private boolean highContrastMode = false;
    private float globalScaleMultiplier = 1.0f;
    private Map<String, ComponentState> layout = new LinkedHashMap<>();

    public String getActiveProfile() { return activeProfile; }
    public void setActiveProfile(String activeProfile) { this.activeProfile = activeProfile; }

    public String getActiveTheme() { return activeTheme; }
    public void setActiveTheme(String activeTheme) { this.activeTheme = activeTheme; }

    public int getToggleHotkeyKeyCode() { return toggleHotkeyKeyCode; }
    public void setToggleHotkeyKeyCode(int toggleHotkeyKeyCode) { this.toggleHotkeyKeyCode = toggleHotkeyKeyCode; }

    public int getResetHotkeyKeyCode() { return resetHotkeyKeyCode; }
    public void setResetHotkeyKeyCode(int resetHotkeyKeyCode) { this.resetHotkeyKeyCode = resetHotkeyKeyCode; }

    public Map<String, ComponentState> getLayout() { return layout; }
    public void setLayout(Map<String, ComponentState> layout) { this.layout = layout; }

    public boolean isGridSnap() { return gridSnap; }
    public void setGridSnap(boolean gridSnap) { this.gridSnap = gridSnap; }

    public int getDefaultGridSize() { return defaultGridSize; }
    public void setDefaultGridSize(int defaultGridSize) { this.defaultGridSize = defaultGridSize; }

    public boolean isSmartGuides() { return smartGuides; }
    public void setSmartGuides(boolean smartGuides) { this.smartGuides = smartGuides; }

    public boolean isAnimations() { return animations; }
    public void setAnimations(boolean animations) { this.animations = animations; }

    public boolean isHighContrastMode() { return highContrastMode; }
    public void setHighContrastMode(boolean highContrastMode) { this.highContrastMode = highContrastMode; }

    public float getGlobalScaleMultiplier() { return globalScaleMultiplier; }
    public void setGlobalScaleMultiplier(float globalScaleMultiplier) { this.globalScaleMultiplier = globalScaleMultiplier; }
}
