package com.nexui.mixin;

import com.nexui.integration.ScreenRelocator;
import com.nexui.model.Rect2i;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Abstract base for screens that draw their background box at
 * {@code (width - imageWidth) / 2}, {@code (height - imageHeight) / 2} instead of
 * reading {@code leftPos}/{@code topPos}. Redirecting those two field reads makes
 * the computed box position equal the moved NexUI component's position, so the box
 * follows the relocator exactly (WYSIWYG) while the dark overlay and everything
 * else stay untouched. Concrete subclasses pick the screens to apply it to.
 */
public abstract class ScreenBackgroundBoxMixin {
    @Shadow
    protected int imageWidth;

    @Shadow
    protected int imageHeight;

    @Redirect(
        method = "extractBackground",
        at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/screens/Screen;width:I", opcode = Opcodes.GETFIELD)
    )
    private int nexui$redirectBoxX(Screen instance, int original) {
        Rect2i target = ScreenRelocator.getRelocationTarget((AbstractContainerScreen<?>) instance);
        if (target == null) {
            return original;
        }
        return target.x() * 2 + imageWidth;
    }

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
