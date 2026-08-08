package com.nexui.config;

import com.nexui.model.Rect2i;
import com.nexui.model.UIComponent;

/**
 * Serializable snapshot of a single UIComponent's placement and properties.
 * Stored per-component so the active profile's layout survives game restarts
 * (Gson-friendly plain DTO).
 */
public class ComponentState {
    public String id;
    public Rect2i defaultBounds;
    public Rect2i currentBounds;
    public float scale;
    public float rotation;
    public boolean visible;
    public boolean locked;
    public int zIndex;

    public static ComponentState from(UIComponent component) {
        ComponentState state = new ComponentState();
        state.id = component.getId();
        state.defaultBounds = component.getDefaultBounds();
        state.currentBounds = component.getCurrentBounds();
        state.scale = component.getScale();
        state.rotation = component.getRotation();
        state.visible = component.isVisible();
        state.locked = component.isLocked();
        state.zIndex = component.getZIndex();
        return state;
    }

    public void applyTo(UIComponent component) {
        component.setDefaultBounds(defaultBounds);
        component.setCurrentBounds(currentBounds);
        component.setScale(scale);
        component.setRotation(rotation);
        component.setVisible(visible);
        component.setLocked(locked);
        component.setZIndex(zIndex);
    }
}
