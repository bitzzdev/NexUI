package com.nexui.model;

/**
 * Supported UI animation styles for entrance, exit, and hover effects.
 */
public enum AnimationType {
    NONE("None", "Static rendering without animations"),
    FADE("Fade", "Smooth opacity transition"),
    SCALE("Scale", "Pop-in scaling effect"),
    SLIDE("Slide", "Directional translation slide"),
    SPRING("Spring", "Physics-based spring response"),
    BOUNCE("Bounce", "Playful bouncing entry"),
    ELASTIC("Elastic", "Fluid elastic damping"),
    OVERSHOOT("Overshoot", "Modern Figma-style overshoot scale");

    private final String displayName;
    private final String description;

    AnimationType(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }
}
