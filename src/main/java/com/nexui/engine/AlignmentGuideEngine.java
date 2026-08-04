package com.nexui.engine;

import com.nexui.model.Rect2i;
import com.nexui.model.UIComponent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Smart alignment guide calculation engine for Figma-like snap lines (Center-X, Center-Y, Top, Bottom, Left, Right).
 */
public class AlignmentGuideEngine {
    private static final int SNAP_THRESHOLD = 6; // pixels

    public record AlignmentGuide(int position, boolean isVertical, String label) {}

    public static class AlignmentResult {
        public final Rect2i bounds;
        public final List<AlignmentGuide> guides;

        public AlignmentResult(Rect2i bounds, List<AlignmentGuide> guides) {
            this.bounds = bounds;
            this.guides = guides;
        }
    }

    public static AlignmentResult computeGuides(UIComponent target, Collection<UIComponent> candidates, int canvasWidth, int canvasHeight) {
        if (target == null) {
            return new AlignmentResult(new Rect2i(0, 0, 10, 10), List.of());
        }

        Rect2i b = target.getCurrentBounds();
        int x = b.x();
        int y = b.y();
        int w = b.width();
        int h = b.height();
        int cx = b.centerX();
        int cy = b.centerY();

        List<AlignmentGuide> activeGuides = new ArrayList<>();

        // Canvas Center Lines
        int screenCenterX = canvasWidth / 2;
        int screenCenterY = canvasHeight / 2;

        if (Math.abs(cx - screenCenterX) <= SNAP_THRESHOLD) {
            x = screenCenterX - w / 2;
            activeGuides.add(new AlignmentGuide(screenCenterX, true, "Canvas Center X"));
        }

        if (Math.abs(cy - screenCenterY) <= SNAP_THRESHOLD) {
            y = screenCenterY - h / 2;
            activeGuides.add(new AlignmentGuide(screenCenterY, false, "Canvas Center Y"));
        }

        // Check against other visible components
        for (UIComponent other : candidates) {
            if (other.getId().equals(target.getId()) || !other.isVisible()) continue;
            Rect2i ob = other.getCurrentBounds();

            // Vertical Guide Checks (X axis)
            if (Math.abs(x - ob.x()) <= SNAP_THRESHOLD) {
                x = ob.x();
                activeGuides.add(new AlignmentGuide(x, true, "Left Align"));
            } else if (Math.abs(x + w - ob.right()) <= SNAP_THRESHOLD) {
                x = ob.right() - w;
                activeGuides.add(new AlignmentGuide(ob.right(), true, "Right Align"));
            } else if (Math.abs(cx - ob.centerX()) <= SNAP_THRESHOLD) {
                x = ob.centerX() - w / 2;
                activeGuides.add(new AlignmentGuide(ob.centerX(), true, "Center X Align"));
            }

            // Horizontal Guide Checks (Y axis)
            if (Math.abs(y - ob.y()) <= SNAP_THRESHOLD) {
                y = ob.y();
                activeGuides.add(new AlignmentGuide(y, false, "Top Align"));
            } else if (Math.abs(y + h - ob.bottom()) <= SNAP_THRESHOLD) {
                y = ob.bottom() - h;
                activeGuides.add(new AlignmentGuide(ob.bottom(), false, "Bottom Align"));
            } else if (Math.abs(cy - ob.centerY()) <= SNAP_THRESHOLD) {
                y = ob.centerY() - h / 2;
                activeGuides.add(new AlignmentGuide(ob.centerY(), false, "Center Y Align"));
            }
        }

        return new AlignmentResult(new Rect2i(x, y, w, h), activeGuides);
    }
}
