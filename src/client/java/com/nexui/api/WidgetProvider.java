package com.nexui.api;

import java.util.List;

/**
 * Provider interface for third-party Fabric mods to register HUD and container UI widgets dynamically.
 */
public interface WidgetProvider {
    String getModId();
    List<NexUIWidget> getProvidedWidgets();
}
