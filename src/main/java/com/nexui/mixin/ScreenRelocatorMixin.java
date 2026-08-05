package com.nexui.mixin;

import com.nexui.integration.ScreenRelocator;
import com.nexui.model.Rect2i;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Places container screens at the position of their moved NexUI component. Runs at
 * the end of {@code AbstractContainerScreen.init}; recipe-book screens clobber
 * {@code leftPos} afterwards, so {@link RecipeBookScreenRelocatorMixin} re-applies
 * the position for them.
 */
@Mixin(AbstractContainerScreen.class)
public abstract class ScreenRelocatorMixin {

    @Inject(method = "init", at = @At("TAIL"))
    private void nexui$offsetContainerScreen(CallbackInfo ci) {
        Rect2i target = ScreenRelocator.getRelocationTarget((AbstractContainerScreen<?>) (Object) this);
        if (target == null) {
            return;
        }
        AbstractContainerScreenAccessor accessor = (AbstractContainerScreenAccessor) (Object) this;
        accessor.nexui$setLeftPos(target.x());
        accessor.nexui$setTopPos(target.y());
    }
}
