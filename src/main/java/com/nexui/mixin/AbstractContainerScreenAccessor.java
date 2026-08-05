package com.nexui.mixin;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * Accessor for fields declared in {@link AbstractContainerScreen} but inherited by
 * subclasses. Mixins targeting the subclasses cannot {@code @Shadow} these fields
 * (they are not declared in the target class), so they go through this interface.
 */
@Mixin(AbstractContainerScreen.class)
public interface AbstractContainerScreenAccessor {
    @Accessor("leftPos")
    void nexui$setLeftPos(int leftPos);

    @Accessor("leftPos")
    int nexui$leftPos();

    @Accessor("topPos")
    void nexui$setTopPos(int topPos);

    @Accessor("imageWidth")
    int nexui$imageWidth();

    @Accessor("imageHeight")
    int nexui$imageHeight();
}
