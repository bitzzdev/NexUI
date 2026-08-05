package com.nexui.mixin;

import com.nexui.integration.ScreenRelocator;
import com.nexui.model.Rect2i;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CraftingScreen;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * The crafting table already blits its box at {@code leftPos} (so the X axis follows
 * the relocator), but computes Y as {@code (height - imageHeight) / 2}. Only the
 * height read is redirected so the vertical position matches the moved component.
 */
@Mixin(CraftingScreen.class)
public abstract class CraftingScreenBackgroundBoxMixin {

    @Redirect(
        method = "extractBackground",
        at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/screens/inventory/CraftingScreen;height:I", opcode = Opcodes.GETFIELD)
    )
    private int nexui$redirectBoxY(CraftingScreen instance, GuiGraphicsExtractor context, int mouseX, int mouseY, float delta) {
        Rect2i target = ScreenRelocator.getRelocationTarget(instance);
        if (target == null) {
            return instance.height;
        }
        return target.y() * 2 + ((AbstractContainerScreenAccessor) instance).nexui$imageHeight();
    }
}
