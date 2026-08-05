package com.nexui.integration;

import com.nexui.model.LayoutProfile;
import com.nexui.model.Rect2i;
import com.nexui.model.UIComponent;
import com.nexui.registry.ProfileRegistry;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.AbstractFurnaceScreen;
import net.minecraft.client.gui.screens.inventory.AnvilScreen;
import net.minecraft.client.gui.screens.inventory.BeaconScreen;
import net.minecraft.client.gui.screens.inventory.BrewingStandScreen;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.client.gui.screens.inventory.CraftingScreen;
import net.minecraft.client.gui.screens.inventory.DispenserScreen;
import net.minecraft.client.gui.screens.inventory.EnchantmentScreen;
import net.minecraft.client.gui.screens.inventory.HopperScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.inventory.LoomScreen;
import net.minecraft.client.gui.screens.inventory.MerchantScreen;
import net.minecraft.client.gui.screens.inventory.ShulkerBoxScreen;
import net.minecraft.client.gui.screens.inventory.SmithingScreen;
import net.minecraft.client.gui.screens.inventory.StonecutterScreen;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Maps container screen classes onto NexUI component ids so the screen relocation
 * mixin knows which component controls each screen's position.
 */
public final class ScreenRelocator {
    private static final Map<Class<?>, String> SCREEN_TO_COMPONENT = new LinkedHashMap<>();

    static {
        SCREEN_TO_COMPONENT.put(InventoryScreen.class, "inventory_gui");
        SCREEN_TO_COMPONENT.put(ContainerScreen.class, "chest_gui");
        SCREEN_TO_COMPONENT.put(ShulkerBoxScreen.class, "chest_gui");
        SCREEN_TO_COMPONENT.put(DispenserScreen.class, "chest_gui");
        SCREEN_TO_COMPONENT.put(HopperScreen.class, "chest_gui");
        SCREEN_TO_COMPONENT.put(AbstractFurnaceScreen.class, "furnace_gui");
        SCREEN_TO_COMPONENT.put(CraftingScreen.class, "crafting_table");
        SCREEN_TO_COMPONENT.put(AnvilScreen.class, "anvil_gui");
        SCREEN_TO_COMPONENT.put(BrewingStandScreen.class, "brewing_stand");
        SCREEN_TO_COMPONENT.put(EnchantmentScreen.class, "enchanting_table");
        SCREEN_TO_COMPONENT.put(BeaconScreen.class, "beacon_gui");
        SCREEN_TO_COMPONENT.put(SmithingScreen.class, "smithing_table");
        SCREEN_TO_COMPONENT.put(LoomScreen.class, "loom_gui");
        SCREEN_TO_COMPONENT.put(StonecutterScreen.class, "stonecutter_gui");
        SCREEN_TO_COMPONENT.put(MerchantScreen.class, "merchant_gui");
    }

    private ScreenRelocator() {
    }

    /**
     * Returns the NexUI component id controlling the given screen's position, or
     * {@code null} if the screen has no NexUI component.
     */
    public static String getComponentId(AbstractContainerScreen<?> screen) {
        Class<?> type = screen.getClass();
        String id = SCREEN_TO_COMPONENT.get(type);
        if (id != null) {
            return id;
        }
        for (Map.Entry<Class<?>, String> entry : SCREEN_TO_COMPONENT.entrySet()) {
            if (entry.getKey().isAssignableFrom(type)) {
                return entry.getValue();
            }
        }
        return null;
    }

    /**
     * Returns the absolute position a screen should be placed at, or {@code null}
     * when the screen has no moved NexUI component (in which case it keeps the
     * vanilla centered position). Only a component that differs from its default
     * bounds counts as moved.
     */
    public static Rect2i getRelocationTarget(AbstractContainerScreen<?> screen) {
        String componentId = getComponentId(screen);
        if (componentId == null) {
            return null;
        }
        LayoutProfile profile = ProfileRegistry.getInstance().getActiveProfile();
        UIComponent component = profile == null ? null : profile.getComponent(componentId);
        if (component == null) {
            return null;
        }
        Rect2i def = component.getDefaultBounds();
        Rect2i cur = component.getCurrentBounds();
        if (cur.x() == def.x() && cur.y() == def.y()) {
            return null;
        }
        return cur;
    }
}
