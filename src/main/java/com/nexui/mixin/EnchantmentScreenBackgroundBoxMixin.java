package com.nexui.mixin;

import net.minecraft.client.gui.screens.inventory.EnchantmentScreen;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EnchantmentScreen.class)
public abstract class EnchantmentScreenBackgroundBoxMixin extends ScreenBackgroundBoxMixin {
}
