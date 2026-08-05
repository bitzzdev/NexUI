package com.nexui.mixin;

import com.nexui.integration.ScreenRelocator;
import com.nexui.model.Rect2i;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.AbstractFurnaceScreen;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Furnace, blast furnace and smoker share {@code AbstractFurnaceScreen}'s
 * {@code getRecipeBookButtonPosition}, which positions the recipe book button from
 * the vanilla-centered {@code leftPos}/{@code height} during {@code init} (before the
 * relocation re-apply). Redirect both reads so the button follows the moved screen box.
 */
@Mixin(AbstractFurnaceScreen.class)
public abstract class AbstractFurnaceScreenRecipeBookButtonMixin {

    @Redirect(
        method = "getRecipeBookButtonPosition",
        at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/screens/inventory/AbstractFurnaceScreen;leftPos:I", opcode = Opcodes.GETFIELD)
    )
    private int nexui$redirectButtonX(AbstractFurnaceScreen instance) {
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
        at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/screens/inventory/AbstractFurnaceScreen;height:I", opcode = Opcodes.GETFIELD)
    )
    private int nexui$redirectButtonY(AbstractFurnaceScreen instance) {
        Rect2i target = ScreenRelocator.getRelocationTarget(instance);
        if (target == null) {
            return instance.height;
        }
        return target.y() * 2 + ((AbstractContainerScreenAccessor) instance).nexui$imageHeight();
    }
}
