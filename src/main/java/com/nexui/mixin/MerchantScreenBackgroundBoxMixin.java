package com.nexui.mixin;

import com.nexui.integration.ScreenRelocator;
import com.nexui.model.Rect2i;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.MerchantScreen;
import net.minecraft.client.input.MouseButtonEvent;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * MerchantScreen computes several positions from {@code (width - imageWidth) / 2} and
 * {@code (height - imageHeight) / 2} instead of reading the relocated {@code leftPos}/
 * {@code topPos}, so parts of the screen ignored the relocation: the background box
 * (extractBackground), the sell-item list, scroller and trade XP bar (extractContents),
 * the clickable trade buttons (init) and the scroller drag zone (mouseClicked).
 * Redirecting the field reads makes each computed origin equal the moved NexUI
 * component's top-left corner (WYSIWYG). The dark overlay and slots are untouched
 * because they are drawn in the super calls, not by these field reads. The FIELD
 * targets use this class as owner because javac emits the field reference against
 * the class that reads it, and each handler mirrors its enclosing method's parameters.
 */
@Mixin(MerchantScreen.class)
public abstract class MerchantScreenBackgroundBoxMixin {

    @Redirect(
        method = {"extractBackground", "extractContents"},
        at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/screens/inventory/MerchantScreen;width:I", opcode = Opcodes.GETFIELD)
    )
    private int nexui$redirectOriginX(MerchantScreen instance, GuiGraphicsExtractor context, int mouseX, int mouseY, float delta) {
        return nexui$substitutedWidth(instance);
    }

    @Redirect(
        method = {"extractBackground", "extractContents"},
        at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/screens/inventory/MerchantScreen;height:I", opcode = Opcodes.GETFIELD)
    )
    private int nexui$redirectOriginY(MerchantScreen instance, GuiGraphicsExtractor context, int mouseX, int mouseY, float delta) {
        return nexui$substitutedHeight(instance);
    }

    @Redirect(
        method = "init",
        at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/screens/inventory/MerchantScreen;width:I", opcode = Opcodes.GETFIELD)
    )
    private int nexui$redirectInitX(MerchantScreen instance) {
        return nexui$substitutedWidth(instance);
    }

    @Redirect(
        method = "init",
        at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/screens/inventory/MerchantScreen;height:I", opcode = Opcodes.GETFIELD)
    )
    private int nexui$redirectInitY(MerchantScreen instance) {
        return nexui$substitutedHeight(instance);
    }

    @Redirect(
        method = "mouseClicked",
        at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/screens/inventory/MerchantScreen;width:I", opcode = Opcodes.GETFIELD)
    )
    private int nexui$redirectClickX(MerchantScreen instance, MouseButtonEvent event, boolean doubleClicked) {
        return nexui$substitutedWidth(instance);
    }

    @Redirect(
        method = "mouseClicked",
        at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/screens/inventory/MerchantScreen;height:I", opcode = Opcodes.GETFIELD)
    )
    private int nexui$redirectClickY(MerchantScreen instance, MouseButtonEvent event, boolean doubleClicked) {
        return nexui$substitutedHeight(instance);
    }

    private static int nexui$substitutedWidth(MerchantScreen instance) {
        Rect2i target = ScreenRelocator.getRelocationTarget(instance);
        if (target == null) {
            return instance.width;
        }
        return target.x() * 2 + ((AbstractContainerScreenAccessor) instance).nexui$imageWidth();
    }

    private static int nexui$substitutedHeight(MerchantScreen instance) {
        Rect2i target = ScreenRelocator.getRelocationTarget(instance);
        if (target == null) {
            return instance.height;
        }
        return target.y() * 2 + ((AbstractContainerScreenAccessor) instance).nexui$imageHeight();
    }
}
