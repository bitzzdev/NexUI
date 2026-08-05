package com.nexui.mixin;

import net.minecraft.client.gui.screens.inventory.BeaconScreen;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BeaconScreen.class)
public abstract class BeaconScreenBackgroundBoxMixin extends ScreenBackgroundBoxMixin {
}
