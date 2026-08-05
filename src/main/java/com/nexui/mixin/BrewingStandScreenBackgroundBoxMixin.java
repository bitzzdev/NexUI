package com.nexui.mixin;

import net.minecraft.client.gui.screens.inventory.BrewingStandScreen;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BrewingStandScreen.class)
public abstract class BrewingStandScreenBackgroundBoxMixin extends ScreenBackgroundBoxMixin {
}
