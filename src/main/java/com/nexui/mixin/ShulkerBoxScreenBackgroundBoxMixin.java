package com.nexui.mixin;

import net.minecraft.client.gui.screens.inventory.ShulkerBoxScreen;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ShulkerBoxScreen.class)
public abstract class ShulkerBoxScreenBackgroundBoxMixin extends ScreenBackgroundBoxMixin {
}
