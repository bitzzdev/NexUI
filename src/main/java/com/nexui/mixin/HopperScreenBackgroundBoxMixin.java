package com.nexui.mixin;

import net.minecraft.client.gui.screens.inventory.HopperScreen;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(HopperScreen.class)
public abstract class HopperScreenBackgroundBoxMixin extends ScreenBackgroundBoxMixin {
}
