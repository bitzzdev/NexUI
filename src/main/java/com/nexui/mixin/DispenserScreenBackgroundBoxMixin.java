package com.nexui.mixin;

import net.minecraft.client.gui.screens.inventory.DispenserScreen;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(DispenserScreen.class)
public abstract class DispenserScreenBackgroundBoxMixin extends ScreenBackgroundBoxMixin {
}
