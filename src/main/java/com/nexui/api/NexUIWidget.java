package com.nexui.api;

import com.nexui.model.ComponentStyle;
import com.nexui.model.Rect2i;

/**
 * Public interface for custom widgets registered into NexUI.
 */
public interface NexUIWidget {
    String getId();
    String getName();
    WidgetCategory getCategory();
    Rect2i getDefaultBounds();

    void render(int x, int y, int width, int height, ComponentStyle style, float tickDelta);
    boolean isAvailable();
}
