package com.nexui.mixin;

import com.nexui.integration.ScreenRelocator;
import com.nexui.model.Rect2i;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.AbstractRecipeBookScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Recipe-book screens ({@code InventoryScreen}, {@code CraftingScreen}, ...) reset
 * {@code leftPos} to the vanilla centered value in {@code AbstractRecipeBookScreen.init}
 * after {@code super.init()} ran. Re-apply the NexUI position at the very end so the
 * moved position sticks.
 */
@Mixin(AbstractRecipeBookScreen.class)
public abstract class RecipeBookScreenRelocatorMixin {
    @Shadow
    public int leftPos;

    @Shadow
    public int topPos;

    @Inject(method = "init", at = @At("TAIL"))
    private void nexui$reapplyOffset(CallbackInfo ci) {
        Rect2i target = ScreenRelocator.getRelocationTarget((AbstractContainerScreen<?>) (Object) this);
        if (target == null) {
            return;
        }
        leftPos = target.x();
        topPos = target.y();
    }
}
