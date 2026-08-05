package com.nexui.mixin;

import com.nexui.integration.ScreenRelocator;
import com.nexui.model.Rect2i;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.AbstractRecipeBookScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Recipe-book screens ({@code InventoryScreen}, {@code CraftingScreen}, ...) reset
 * {@code leftPos} to the vanilla centered value in {@code AbstractRecipeBookScreen.init}
 * after {@code super.init()} ran. Re-apply the NexUI position at the very end so the
 * moved position sticks. Fields are accessed through {@link AbstractContainerScreenAccessor}
 * because {@code leftPos}/{@code topPos} are declared on the superclass.
 */
@Mixin(AbstractRecipeBookScreen.class)
public abstract class RecipeBookScreenRelocatorMixin {

    @Inject(method = "init", at = @At("TAIL"))
    private void nexui$reapplyOffset(CallbackInfo ci) {
        Rect2i target = ScreenRelocator.getRelocationTarget((AbstractContainerScreen<?>) (Object) this);
        if (target == null) {
            return;
        }
        AbstractContainerScreenAccessor accessor = (AbstractContainerScreenAccessor) (Object) this;
        accessor.nexui$setLeftPos(target.x());
        accessor.nexui$setTopPos(target.y());
    }
}
