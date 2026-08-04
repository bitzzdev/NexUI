package com.nexui.registry;

import com.nexui.api.NexUIWidget;
import com.nexui.api.WidgetCategory;
import com.nexui.model.ComponentStyle;
import com.nexui.model.Rect2i;
import com.nexui.model.UIComponent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Registry managing default vanilla HUD & GUI components as well as 3rd-party mod widgets.
 */
public class WidgetRegistry {
    private static final WidgetRegistry INSTANCE = new WidgetRegistry();
    private final Map<String, UIComponent> defaultComponents = new LinkedHashMap<>();
    private final Map<String, NexUIWidget> customWidgets = new LinkedHashMap<>();

    private WidgetRegistry() {
        registerDefaultVanillaWidgets();
    }

    public static WidgetRegistry getInstance() {
        return INSTANCE;
    }

    private void registerDefaultVanillaWidgets() {
        // Main HUD Elements
        registerVanilla("hotbar", "Hotbar", WidgetCategory.HOTBAR, new Rect2i(200, 480, 360, 44));
        registerVanilla("hearts", "Health Hearts", WidgetCategory.HEALTH_BARS, new Rect2i(200, 460, 160, 18));
        registerVanilla("hunger", "Hunger Bar", WidgetCategory.HEALTH_BARS, new Rect2i(400, 460, 160, 18));
        registerVanilla("armor", "Armor Bar", WidgetCategory.HEALTH_BARS, new Rect2i(200, 440, 160, 18));
        registerVanilla("xp_bar", "Experience Bar", WidgetCategory.HOTBAR, new Rect2i(200, 475, 360, 10));
        registerVanilla("xp_level", "XP Level Indicator", WidgetCategory.HOTBAR, new Rect2i(370, 460, 30, 16));
        registerVanilla("crosshair", "Crosshair", WidgetCategory.HOTBAR, new Rect2i(370, 260, 20, 20));
        registerVanilla("air_bar", "Air Bubble Bar", WidgetCategory.HEALTH_BARS, new Rect2i(400, 440, 160, 18));
        registerVanilla("mount_hud", "Mount Jump / Health", WidgetCategory.HEALTH_BARS, new Rect2i(200, 440, 360, 18));

        // Action & Info Overlays
        registerVanilla("action_bar", "Action Bar Text", WidgetCategory.ACTION_BAR, new Rect2i(240, 400, 280, 24));
        registerVanilla("chat", "Chat Window", WidgetCategory.ACTION_BAR, new Rect2i(10, 320, 320, 140));
        registerVanilla("scoreboard", "Scoreboard Sidebar", WidgetCategory.ACTION_BAR, new Rect2i(600, 150, 150, 200));
        registerVanilla("titles", "Title & Subtitle", WidgetCategory.ACTION_BAR, new Rect2i(200, 120, 360, 60));
        registerVanilla("boss_bars", "Boss Health Bars", WidgetCategory.NOTIFICATIONS, new Rect2i(200, 10, 360, 40));
        registerVanilla("toast_notifications", "Toast Notifications", WidgetCategory.NOTIFICATIONS, new Rect2i(580, 10, 160, 60));

        // Containers & Screens
        registerVanilla("recipe_book", "Recipe Book Overlay", WidgetCategory.CONTAINER_GUI, new Rect2i(40, 100, 160, 220));
        registerVanilla("inventory_gui", "Inventory Screen", WidgetCategory.CONTAINER_GUI, new Rect2i(240, 140, 280, 220));
        registerVanilla("chest_gui", "Chest Screen", WidgetCategory.CONTAINER_GUI, new Rect2i(240, 100, 280, 260));
        registerVanilla("furnace_gui", "Furnace Screen", WidgetCategory.CONTAINER_GUI, new Rect2i(240, 140, 280, 220));
        registerVanilla("crafting_table", "Crafting Table Screen", WidgetCategory.CONTAINER_GUI, new Rect2i(240, 140, 280, 220));
        registerVanilla("anvil_gui", "Anvil Screen", WidgetCategory.CONTAINER_GUI, new Rect2i(240, 140, 280, 220));
        registerVanilla("brewing_stand", "Brewing Stand Screen", WidgetCategory.CONTAINER_GUI, new Rect2i(240, 140, 280, 220));
        registerVanilla("enchanting_table", "Enchanting Table Screen", WidgetCategory.CONTAINER_GUI, new Rect2i(240, 140, 280, 220));
        registerVanilla("beacon_gui", "Beacon Screen", WidgetCategory.CONTAINER_GUI, new Rect2i(240, 140, 280, 220));
        registerVanilla("smithing_table", "Smithing Table Screen", WidgetCategory.CONTAINER_GUI, new Rect2i(240, 140, 280, 220));
        registerVanilla("loom_gui", "Loom Screen", WidgetCategory.CONTAINER_GUI, new Rect2i(240, 140, 280, 220));
        registerVanilla("stonecutter_gui", "Stonecutter Screen", WidgetCategory.CONTAINER_GUI, new Rect2i(240, 140, 280, 220));
        registerVanilla("merchant_gui", "Villager Merchant Screen", WidgetCategory.CONTAINER_GUI, new Rect2i(200, 120, 360, 260));
    }

    private void registerVanilla(String id, String name, WidgetCategory category, Rect2i bounds) {
        UIComponent comp = new UIComponent(id, name, category, bounds);
        defaultComponents.put(id, comp);
    }

    public void registerWidget(NexUIWidget widget) {
        if (widget != null) {
            customWidgets.put(widget.getId(), widget);
            UIComponent comp = new UIComponent(widget.getId(), widget.getName(), widget.getCategory(), widget.getDefaultBounds());
            defaultComponents.put(widget.getId(), comp);
        }
    }

    public Map<String, UIComponent> getDefaultComponents() {
        Map<String, UIComponent> copy = new LinkedHashMap<>();
        for (Map.Entry<String, UIComponent> entry : defaultComponents.entrySet()) {
            copy.put(entry.getKey(), entry.getValue().copy());
        }
        return copy;
    }

    public List<UIComponent> getAllAsList() {
        return new ArrayList<>(getDefaultComponents().values());
    }

    public NexUIWidget getCustomWidget(String id) {
        return customWidgets.get(id);
    }
}
