package com.nexui.api;

import com.nexui.model.Theme;
import com.nexui.registry.ThemeRegistry;
import com.nexui.registry.WidgetRegistry;

/**
 * Public API for third-party Fabric mods to integrate with NexUI.
 *
 * <p>Example usage:
 * <pre>{@code
 * NexUIAPI.registerWidget(new MyCustomModWidget());
 * NexUIAPI.registerTheme(new MyCustomTheme());
 * }</pre>
 */
public final class NexUIAPI {
    private NexUIAPI() {}

    /**
     * Registers a new HUD or container widget into NexUI studio.
     */
    public static void registerWidget(NexUIWidget widget) {
        WidgetRegistry.getInstance().registerWidget(widget);
    }

    /**
     * Registers a custom widget provider.
     */
    public static void registerProvider(WidgetProvider provider) {
        if (provider != null) {
            for (NexUIWidget widget : provider.getProvidedWidgets()) {
                registerWidget(widget);
            }
        }
    }

    /**
     * Registers a custom theme.
     */
    public static void registerTheme(Theme theme) {
        ThemeRegistry.getInstance().registerTheme(theme);
    }
}
