package com.nexui.mixin;

import com.nexui.integration.ScreenRelocator;
import com.nexui.model.LayoutProfile;
import com.nexui.model.Rect2i;
import com.nexui.model.UIComponent;
import com.nexui.registry.ProfileRegistry;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Repositions container screens (inventory, chest, furnaces, ...) to the position
 * the matching NexUI component was moved to. Position is applied based only on the
 * moved bounds; the visibility flag does not affect the real screen (it only hides
 * the relocator box on the design canvas).
 */
@Mixin(AbstractContainerScreen.class)
public abstract class ScreenRelocatorMixin {
    private static final Logger LOGGER = LoggerFactory.getLogger("NexUI-Screen");
    private static boolean logged = false;

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
        if (component == null) {
            return;
        }

        Rect2i def = component.getDefaultBounds();
        Rect2i cur = component.getCurrentBounds();

        if (cur.x() != def.x() || cur.y() != def.y()) {
            // Absolute WYSIWYG: a moved component places the real screen exactly
            // where its box sits on the design canvas. Untouched components keep
            // the vanilla centered position.
            if (!logged) {
                logged = true;
                LOGGER.info("NexUI: moving screen '{}' to ({}, {})", componentId, cur.x(), cur.y());
            }
            leftPos = cur.x();
            topPos = cur.y();
        }
    }
}
