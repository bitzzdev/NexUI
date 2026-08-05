package com.nexui.mixin;

import com.nexui.integration.ScreenRelocator;
import com.nexui.model.Rect2i;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CraftingScreen;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * The crafting table already blits its box at {@code leftPos} (so the X axis follows
 * the relocator), but computes Y as {@code (height - imageHeight) / 2}. Only the
 * height read is redirected so the vertical position matches the moved component.
 */
@Mixin(CraftingScreen.class)
public abstract class CraftingScreenBackgroundBoxMixin {
    @Shadow
    protected int imageHeight;

    @Redirect(
        method = "extractBackground",
        at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/screens/Screen;height:I", opcode = Opcodes.GETFIELD)
    )
    private int nexui$redirectBoxY(Screen instance, int original) {
        Rect2i target = ScreenRelocator.getRelocationTarget((AbstractContainerScreen<?>) instance);
        if (target == null) {
            return original;
        }
        return target.y() * 2 + imageHeight;
    }
}
