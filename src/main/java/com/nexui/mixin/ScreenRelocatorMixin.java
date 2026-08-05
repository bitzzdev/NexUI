package com.nexui.mixin;

import com.nexui.integration.ScreenRelocator;
import com.nexui.model.LayoutProfile;
import com.nexui.model.Rect2i;
import com.nexui.model.UIComponent;
import com.nexui.registry.ProfileRegistry;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Offsets the top-left position of container screens (inventory, chest, furnaces, ...)
 * by the delta the matching NexUI component has been moved from its default bounds.
 */
@Mixin(AbstractContainerScreen.class)
public abstract class ScreenRelocatorMixin {
    @Shadow
    public int leftPos;

    @Shadow
    public int topPos;

    @Inject(method = "init", at = @At("TAIL"))
    private void nexui$offsetContainerScreen(CallbackInfo ci) {
        String componentId = ScreenRelocator.getComponentId((AbstractContainerScreen<?>) (Object) this);
        if (componentId == null) {
            return;
        }

        LayoutProfile profile = ProfileRegistry.getInstance().getActiveProfile();
        UIComponent component = profile == null ? null : profile.getComponent(componentId);
        if (component == null || !component.isVisible()) {
            return;
        }

        Rect2i def = component.getDefaultBounds();
        Rect2i cur = component.getCurrentBounds();
        leftPos += cur.x() - def.x();
        topPos += cur.y() - def.y();
    }
}
