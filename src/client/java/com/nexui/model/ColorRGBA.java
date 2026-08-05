package com.nexui.model;

/**
 * Modern RGBA color abstraction supporting Hex string parsing, HSL conversion, alpha blending, and gradients.
 */
public record ColorRGBA(int red, int green, int blue, int alpha) {
    public static final ColorRGBA WHITE = new ColorRGBA(255, 255, 255, 255);
    public static final ColorRGBA BLACK = new ColorRGBA(0, 0, 0, 255);
    public static final ColorRGBA TRANSPARENT = new ColorRGBA(0, 0, 0, 0);
    public static final ColorRGBA ACCENT_BLUE = new ColorRGBA(99, 102, 241, 255); // Vibrant Indigo
    public static final ColorRGBA ACCENT_CYAN = new ColorRGBA(6, 182, 212, 255);  // Neon Cyan
    public static final ColorRGBA ACCENT_PINK = new ColorRGBA(236, 72, 153, 255); // Neon Pink
    public static final ColorRGBA DARK_BG = new ColorRGBA(18, 18, 24, 220);       // Sleek Dark Glass

    public ColorRGBA {
        red = Math.clamp(red, 0, 255);
        green = Math.clamp(green, 0, 255);
        blue = Math.clamp(blue, 0, 255);
        alpha = Math.clamp(alpha, 0, 255);
    }

    public static ColorRGBA fromHex(String hex) {
        if (hex == null || hex.isEmpty()) return WHITE;
        String clean = hex.startsWith("#") ? hex.substring(1) : hex;
        try {
            if (clean.length() == 6) {
                int r = Integer.parseInt(clean.substring(0, 2), 16);
                int g = Integer.parseInt(clean.substring(2, 4), 16);
                int b = Integer.parseInt(clean.substring(4, 6), 16);
                return new ColorRGBA(r, g, b, 255);
            } else if (clean.length() == 8) {
                int r = Integer.parseInt(clean.substring(0, 2), 16);
                int g = Integer.parseInt(clean.substring(2, 4), 16);
                int b = Integer.parseInt(clean.substring(4, 6), 16);
                int a = Integer.parseInt(clean.substring(6, 8), 16);
                return new ColorRGBA(r, g, b, a);
            }
        } catch (NumberFormatException ignored) {}
        return WHITE;
    }

    public String toHex() {
        return String.format("#%02X%02X%02X%02X", red, green, blue, alpha);
    }

    public int toARGB() {
        return (alpha << 24) | (red << 16) | (green << 8) | blue;
    }

    public ColorRGBA withAlpha(int newAlpha) {
        return new ColorRGBA(this.red, this.green, this.blue, newAlpha);
    }

    public ColorRGBA lerp(ColorRGBA target, float delta) {
        float t = Math.clamp(delta, 0f, 1f);
        int r = (int) (this.red + (target.red - this.red) * t);
        int g = (int) (this.green + (target.green - this.green) * t);
        int b = (int) (this.blue + (target.blue - this.blue) * t);
        int a = (int) (this.alpha + (target.alpha - this.alpha) * t);
        return new ColorRGBA(r, g, b, a);
    }
}
