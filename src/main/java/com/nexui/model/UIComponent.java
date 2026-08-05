package com.nexui.model;

import com.nexui.api.WidgetCategory;

/**
 * Representation of a customizable UI component in NexUI studio.
 */
public class UIComponent {
    private final String id;
    private String name;
    private WidgetCategory category;
    private Rect2i defaultBounds;
    private Rect2i currentBounds;

    private float scale = 1.0f;
    private float rotation = 0.0f; // in degrees
    private boolean visible = true;
    private boolean locked = false;
    private int zIndex = 0;

    private ComponentStyle style = new ComponentStyle();

    public UIComponent(String id, String name, WidgetCategory category, Rect2i defaultBounds) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.defaultBounds = defaultBounds;
        this.currentBounds = defaultBounds;
    }

    public UIComponent copy() {
        UIComponent copy = new UIComponent(id, name, category, defaultBounds);
        copy.currentBounds = this.currentBounds;
        copy.scale = this.scale;
        copy.rotation = this.rotation;
        copy.visible = this.visible;
        copy.locked = this.locked;
        copy.zIndex = this.zIndex;
        copy.style = this.style.copy();
        return copy;
    }

    public void reset() {
        this.currentBounds = defaultBounds;
        this.scale = 1.0f;
        this.rotation = 0.0f;
        this.visible = true;
        this.locked = false;
        this.style = new ComponentStyle();
    }

    // Getters & Setters
    public String getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public WidgetCategory getCategory() { return category; }
    public void setCategory(WidgetCategory category) { this.category = category; }

    public Rect2i getDefaultBounds() { return defaultBounds; }
    public Rect2i getCurrentBounds() { return currentBounds; }
    public void setCurrentBounds(Rect2i bounds) { this.currentBounds = bounds; }

    public float getScale() { return scale; }
    public void setScale(float scale) { this.scale = Math.clamp(scale, 0.2f, 5.0f); }

    public float getRotation() { return rotation; }
    public void setRotation(float rotation) { this.rotation = rotation % 360f; }

    public boolean isVisible() { return visible; }
    public void setVisible(boolean visible) { this.visible = visible; }

    public boolean isLocked() { return locked; }
    public void setLocked(boolean locked) { this.locked = locked; }

    public int getZIndex() { return zIndex; }
    public void setZIndex(int zIndex) { this.zIndex = zIndex; }

    public ComponentStyle getStyle() { return style; }
    public void setStyle(ComponentStyle style) { this.style = style; }
}
