package com.nexui.engine;

import com.nexui.model.ColorRGBA;
import com.nexui.model.ComponentStyle;
import com.nexui.model.Rect2i;
import net.minecraft.client.gui.GuiGraphics;

/**
 * Modern UI Rendering Pipeline supporting glassmorphism, smooth borders, outlines, and alignment guides.
 */
public class RenderPipeline {

    public static void renderStyledPanel(GuiGraphics context, Rect2i bounds, ComponentStyle style) {
        if (bounds == null || style == null) return;

        int x = bounds.x();
        int y = bounds.y();
        int w = bounds.width();
        int h = bounds.height();

        // 1. Render Background Glass Box
        context.fill(x, y, x + w, y + h, style.getBackgroundColor().toARGB());

        // 2. Render Border
        if (style.getBorderWidth() > 0) {
            int bw = style.getBorderWidth();
            int bc = style.getBorderColor().toARGB();
            context.fill(x, y, x + w, y + bw, bc); // Top
            context.fill(x, y + h - bw, x + w, y + h, bc); // Bottom
            context.fill(x, y, x + bw, y + h, bc); // Left
            context.fill(x + w - bw, y, x + w, y + h, bc); // Right
        }
    }

    public static void renderSelectionOutline(GuiGraphics context, Rect2i bounds, boolean isPrimary) {
        if (bounds == null) return;
        int color = isPrimary ? ColorRGBA.ACCENT_CYAN.toARGB() : ColorRGBA.ACCENT_BLUE.toARGB();
        int bw = 2;
        int x = bounds.x() - bw;
        int y = bounds.y() - bw;
        int w = bounds.width() + (bw * 2);
        int h = bounds.height() + (bw * 2);

        context.fill(x, y, x + w, y + bw, color);
        context.fill(x, y + h - bw, x + w, y + h, color);
        context.fill(x, y, x + bw, y + h, color);
        context.fill(x + w - bw, y, x + w, y + h, color);
    }

    public static void renderAlignmentGuide(GuiGraphics context, AlignmentGuideEngine.AlignmentGuide guide, int screenWidth, int screenHeight) {
        if (guide == null) return;
        int color = ColorRGBA.ACCENT_PINK.toARGB();
        if (guide.isVertical) {
            context.fill(guide.position, 0, guide.position + 1, screenHeight, color);
        } else {
            context.fill(0, guide.position, screenWidth, guide.position + 1, color);
        }
    }
}
