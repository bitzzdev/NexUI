package com.nexui.engine;

import com.nexui.model.Rect2i;

/**
 * Grid snap engine supporting 4px, 8px, 16px, 32px snapping grids.
 */
public class GridSnapEngine {

    public static int snapValue(int val, int gridSize) {
        if (gridSize <= 1) return val;
        int half = gridSize / 2;
        int rem = val % gridSize;
        if (rem < 0) rem += gridSize;
        if (rem >= half) {
            return val + (gridSize - rem);
        } else {
            return val - rem;
        }
    }

    public static Rect2i snapRect(Rect2i rect, int gridSize) {
        if (gridSize <= 1) return rect;
        int newX = snapValue(rect.x(), gridSize);
        int newY = snapValue(rect.y(), gridSize);
        int newW = Math.max(gridSize, snapValue(rect.width(), gridSize));
        int newH = Math.max(gridSize, snapValue(rect.height(), gridSize));
        return new Rect2i(newX, newY, newW, newH);
    }
}
