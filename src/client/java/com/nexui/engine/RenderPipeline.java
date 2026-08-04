package com.nexui.engine;

import com.nexui.model.ColorRGBA;
import com.nexui.model.ComponentStyle;
import com.nexui.model.Rect2i;
import net.minecraft.client.gui.DrawContext;

/**
 * Modern rendering pipeline for NexUI (Rounded Rectangles, Soft Shadows, Glows, Glassmorphism).
 */
public class RenderPipeline {

    public static void renderStyledPanel(DrawContext context, Rect2i bounds, ComponentStyle style) {
        if (bounds == null || style == null) return;
        int x = bounds.x();
        int y = bounds.y();
        int w = bounds.width();
        int h = bounds.height();

        // Render Drop Shadow if radius > 0
        if (style.getShadowRadius() > 0) {
            ColorRGBA shadow = style.getShadowColor();
            int offset = style.getShadowRadius() / 2;
            context.fill(x - offset, y - offset, x + w + offset, y + h + offset, shadow.toARGB());
        }

        // Render Outer Glow if radius > 0
        if (style.getGlowRadius() > 0) {
            ColorRGBA glow = style.getGlowColor();
            int glowOffset = style.getGlowRadius();
            context.fill(x - glowOffset, y - glowOffset, x + w + glowOffset, y + h + glowOffset, glow.toARGB());
        }

        // Render Glass / Main Background Panel
        ColorRGBA bg = style.getBackgroundColor();
        context.fill(x, y, x + w, y + h, bg.toARGB());

        // Render Border
        if (style.getBorderWidth() > 0) {
            ColorRGBA border = style.getBorderColor();
            int bw = style.getBorderWidth();
            context.fill(x, y, x + w, y + bw, border.toARGB());                             // Top
            context.fill(x, y + h - bw, x + w, y + h, border.toARGB());                     // Bottom
            context.fill(x, y + bw, x + bw, y + h - bw, border.toARGB());                 // Left
            context.fill(x + w - bw, y + bw, x + w, y + h - bw, border.toARGB());         // Right
        }
    }

    public static void renderSelectionOutline(DrawContext context, Rect2i bounds, boolean isPrimary) {
        if (bounds == null) return;
        int x = bounds.x();
        int y = bounds.y();
        int w = bounds.width();
        int h = bounds.height();

        ColorRGBA outlineColor = isPrimary ? ColorRGBA.ACCENT_BLUE : ColorRGBA.ACCENT_CYAN;
        int argb = outlineColor.toARGB();

        // Dashed / Solid Selection Box
        context.fill(x - 2, y - 2, x + w + 2, y - 1, argb);
        context.fill(x - 2, y + h + 1, x + w + 2, y + h + 2, argb);
        context.fill(x - 2, y - 1, x - 1, y + h + 1, argb);
        context.fill(x + w + 1, y - 1, x + w + 2, y + h + 1, argb);

        // Corner Resize Handles (Figma style)
        int handleSize = 6;
        renderHandle(context, x - handleSize / 2, y - handleSize / 2, handleSize, argb);
        renderHandle(context, x + w - handleSize / 2, y - handleSize / 2, handleSize, argb);
        renderHandle(context, x - handleSize / 2, y + h - handleSize / 2, handleSize, argb);
        renderHandle(context, x + w - handleSize / 2, y + h - handleSize / 2, handleSize, argb);
    }

    private static void renderHandle(DrawContext context, int x, int y, int size, int argb) {
        context.fill(x, y, x + size, y + size, ColorRGBA.WHITE.toARGB());
        context.fill(x + 1, y + 1, x + size - 1, y + size - 1, argb);
    }

    public static void renderAlignmentGuide(DrawContext context, AlignmentGuideEngine.AlignmentGuide guide, int width, int height) {
        int color = ColorRGBA.ACCENT_CYAN.toARGB();
        if (guide.isVertical()) {
            context.fill(guide.position(), 0, guide.position() + 1, height, color);
        } else {
            context.fill(0, guide.position(), width, guide.position() + 1, color);
        }
    }
}
