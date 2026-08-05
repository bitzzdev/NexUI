package com.nexui.mixin;

import net.minecraft.client.gui.screens.inventory.MerchantScreen;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(MerchantScreen.class)
public abstract class MerchantScreenBackgroundBoxMixin extends ScreenBackgroundBoxMixin {
}
