package com.nexui.mixin;

import com.nexui.integration.ScreenRelocator;
import com.nexui.model.Rect2i;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * The recipe book button on the survival inventory is positioned once in
 * {@code AbstractRecipeBookScreen.initButton} via {@code getRecipeBookButtonPosition},
 * which reads the still-vanilla {@code leftPos}/{@code height} before the relocation
 * re-apply runs. Redirect both reads so the button follows the moved screen box:
 * X keeps its vanilla book-open offset relative to the box, Y uses the box's vertical
 * center instead of the full screen's.
 */
@Mixin(InventoryScreen.class)
public abstract class InventoryScreenRecipeBookButtonMixin {

    @Redirect(
        method = "getRecipeBookButtonPosition",
        at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/screens/inventory/InventoryScreen;leftPos:I", opcode = Opcodes.GETFIELD)
    )
    private int nexui$redirectButtonX(InventoryScreen instance) {
        Rect2i target = ScreenRelocator.getRelocationTarget(instance);
        AbstractContainerScreenAccessor accessor = (AbstractContainerScreenAccessor) instance;
        if (target == null) {
            return accessor.nexui$leftPos();
        }
        int defaultX = (instance.width - accessor.nexui$imageWidth()) / 2;
        return accessor.nexui$leftPos() + (target.x() - defaultX);
    }

    @Redirect(
        method = "getRecipeBookButtonPosition",
        at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/screens/inventory/InventoryScreen;height:I", opcode = Opcodes.GETFIELD)
    )
    private int nexui$redirectButtonY(InventoryScreen instance) {
        Rect2i target = ScreenRelocator.getRelocationTarget(instance);
        if (target == null) {
            return instance.height;
        }
        return target.y() * 2 + ((AbstractContainerScreenAccessor) instance).nexui$imageHeight();
    }
}
