package com.nexui.model;

import com.nexui.integration.VanillaHudGeometry;

import java.util.HashMap;
import java.util.Map;

/**
 * Interface layout profile containing component layout positions, active theme ID, grid configuration, and custom scaling presets.
 */
public class LayoutProfile {
    private final String id;
    private String name;
    private String description;
    private String themeId;
    private int gridSize;
    private boolean gridSnapEnabled;
    private boolean smartGuidesEnabled;
    private boolean animationsEnabled;
    private final Map<String, UIComponent> components = new HashMap<>();

    public LayoutProfile(String id, String name, String description, String themeId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.themeId = themeId;
        this.gridSize = 8;
        this.gridSnapEnabled = true;
        this.smartGuidesEnabled = true;
        this.animationsEnabled = true;
    }

    public LayoutProfile copy(String newId, String newName) {
        LayoutProfile copy = new LayoutProfile(newId, newName, this.description, this.themeId);
        copy.gridSize = this.gridSize;
        copy.gridSnapEnabled = this.gridSnapEnabled;
        copy.smartGuidesEnabled = this.smartGuidesEnabled;
        copy.animationsEnabled = this.animationsEnabled;
        for (Map.Entry<String, UIComponent> entry : this.components.entrySet()) {
            copy.components.put(entry.getKey(), entry.getValue().copy());
        }
        return copy;
    }

    public void addComponent(UIComponent component) {
        components.put(component.getId(), component);
    }

    /**
     * Re-anchors every component onto the position where vanilla actually renders
     * the element at the given scaled canvas size, preserving each component's
     * user offset (current - default). This keeps relocator boxes overlaid on the
     * real elements regardless of window resolution.
     */
    public void rebaseToWindow(int canvasWidth, int canvasHeight) {
        for (UIComponent comp : components.values()) {
            Rect2i def = comp.getDefaultBounds();
            Rect2i cur = comp.getCurrentBounds();
            int dx = cur.x() - def.x();
            int dy = cur.y() - def.y();

            Rect2i anchor = VanillaHudGeometry.anchorFor(comp.getId(), canvasWidth, canvasHeight);
            if (anchor == null) {
                anchor = VanillaHudGeometry.centeredAnchor(canvasWidth, canvasHeight, def);
            }

            comp.setDefaultBounds(anchor);
            comp.setCurrentBounds(new Rect2i(anchor.x() + dx, anchor.y() + dy, anchor.width(), anchor.height()));
        }
    }

    public UIComponent getComponent(String componentId) {
        return components.get(componentId);
    }

    public Map<String, UIComponent> getComponents() {
        return components;
    }

    // Getters & Setters
    public String getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getThemeId() { return themeId; }
    public void setThemeId(String themeId) { this.themeId = themeId; }

    public int getGridSize() { return gridSize; }
    public void setGridSize(int gridSize) { this.gridSize = Math.max(1, gridSize); }

    public boolean isGridSnapEnabled() { return gridSnapEnabled; }
    public void setGridSnapEnabled(boolean gridSnapEnabled) { this.gridSnapEnabled = gridSnapEnabled; }

    public boolean isSmartGuidesEnabled() { return smartGuidesEnabled; }
    public void setSmartGuidesEnabled(boolean smartGuidesEnabled) { this.smartGuidesEnabled = smartGuidesEnabled; }

    public boolean isAnimationsEnabled() { return animationsEnabled; }
    public void setAnimationsEnabled(boolean animationsEnabled) { this.animationsEnabled = animationsEnabled; }
}
