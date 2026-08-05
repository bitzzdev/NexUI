package com.nexui.mixin;

import com.nexui.integration.ScreenRelocator;
import com.nexui.model.Rect2i;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.ShulkerBoxScreen;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * ShulkerBoxScreen draws its background box at {@code (width - imageWidth) / 2} instead of
 * reading {@code leftPos}, so the box ignored the relocation while slots and text
 * moved. Redirecting the two field reads makes the computed box position equal the
 * moved NexUI component's position (WYSIWYG); the dark overlay is untouched because
 * it is drawn in the super call, not by these field reads. The FIELD targets use
 * this class as owner because javac emits the field reference against the class
 * that reads it.
 */
@Mixin(ShulkerBoxScreen.class)
public abstract class ShulkerBoxScreenBackgroundBoxMixin {

    @Redirect(
        method = "extractBackground",
        at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/screens/inventory/ShulkerBoxScreen;width:I", opcode = Opcodes.GETFIELD)
    )
    private int nexui$redirectBoxX(ShulkerBoxScreen instance, int original) {
        Rect2i target = ScreenRelocator.getRelocationTarget((AbstractContainerScreen<?>) instance);
        if (target == null) {
            return original;
        }
        return target.x() * 2 + ((AbstractContainerScreenAccessor) instance).nexui$imageWidth();
    }

    @Redirect(
        method = "extractBackground",
        at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/screens/inventory/ShulkerBoxScreen;height:I", opcode = Opcodes.GETFIELD)
    )
    private int nexui$redirectBoxY(ShulkerBoxScreen instance, int original) {
        Rect2i target = ScreenRelocator.getRelocationTarget((AbstractContainerScreen<?>) instance);
        if (target == null) {
            return original;
        }
        return target.y() * 2 + ((AbstractContainerScreenAccessor) instance).nexui$imageHeight();
    }
}
