package com.nexui.mixin;

import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ContainerScreen.class)
public abstract class ContainerScreenBackgroundBoxMixin extends ScreenBackgroundBoxMixin {
}
