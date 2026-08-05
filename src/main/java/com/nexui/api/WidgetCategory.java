package com.nexui.api;

/**
 * Categories of UI components supported by NexUI studio.
 */
public enum WidgetCategory {
    HOTBAR("Hotbar & Main HUD"),
    HEALTH_BARS("Player Vitals & Stats"),
    ACTION_BAR("Action & Status Messages"),
    NOTIFICATIONS("Toasts & Bossbars"),
    CONTAINER_GUI("Inventory & Crafting Screens"),
    CUSTOM("Third-Party Mod Components");

    private final String label;

    WidgetCategory(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
