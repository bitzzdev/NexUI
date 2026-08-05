package com.nexui.model;

/**
 * Styling parameters for NexUI components (colors, rounded corners, blur, border, shadow, glow, fonts).
 */
public class ComponentStyle {
    private ColorRGBA backgroundColor = new ColorRGBA(24, 24, 32, 180);
    private ColorRGBA borderColor = new ColorRGBA(99, 102, 241, 140);
    private ColorRGBA shadowColor = new ColorRGBA(0, 0, 0, 80);
    private ColorRGBA glowColor = new ColorRGBA(6, 182, 212, 100);
    private ColorRGBA textColor = ColorRGBA.WHITE;

    private int borderRadius = 8;
    private int borderWidth = 1;
    private int blurRadius = 4;
    private int shadowRadius = 6;
    private int glowRadius = 0;

    private float opacity = 1.0f;
    private float fontSize = 1.0f;
    private String fontFamily = "Inter";
    private int padding = 4;
    private int margin = 2;
    private int spacing = 4;
    private float iconScale = 1.0f;
    private AnimationType animationType = AnimationType.FADE;

    public ComponentStyle() {}

    public ComponentStyle copy() {
        ComponentStyle copy = new ComponentStyle();
        copy.backgroundColor = this.backgroundColor;
        copy.borderColor = this.borderColor;
        copy.shadowColor = this.shadowColor;
        copy.glowColor = this.glowColor;
        copy.textColor = this.textColor;
        copy.borderRadius = this.borderRadius;
        copy.borderWidth = this.borderWidth;
        copy.blurRadius = this.blurRadius;
        copy.shadowRadius = this.shadowRadius;
        copy.glowRadius = this.glowRadius;
        copy.opacity = this.opacity;
        copy.fontSize = this.fontSize;
        copy.fontFamily = this.fontFamily;
        copy.padding = this.padding;
        copy.margin = this.margin;
        copy.spacing = this.spacing;
        copy.iconScale = this.iconScale;
        copy.animationType = this.animationType;
        return copy;
    }

    // Getters and Setters
    public ColorRGBA getBackgroundColor() { return backgroundColor; }
    public void setBackgroundColor(ColorRGBA backgroundColor) { this.backgroundColor = backgroundColor; }

    public ColorRGBA getBorderColor() { return borderColor; }
    public void setBorderColor(ColorRGBA borderColor) { this.borderColor = borderColor; }

    public ColorRGBA getShadowColor() { return shadowColor; }
    public void setShadowColor(ColorRGBA shadowColor) { this.shadowColor = shadowColor; }

    public ColorRGBA getGlowColor() { return glowColor; }
    public void setGlowColor(ColorRGBA glowColor) { this.glowColor = glowColor; }

    public ColorRGBA getTextColor() { return textColor; }
    public void setTextColor(ColorRGBA textColor) { this.textColor = textColor; }

    public int getBorderRadius() { return borderRadius; }
    public void setBorderRadius(int borderRadius) { this.borderRadius = Math.max(0, borderRadius); }

    public int getBorderWidth() { return borderWidth; }
    public void setBorderWidth(int borderWidth) { this.borderWidth = Math.max(0, borderWidth); }

    public int getBlurRadius() { return blurRadius; }
    public void setBlurRadius(int blurRadius) { this.blurRadius = Math.max(0, blurRadius); }

    public int getShadowRadius() { return shadowRadius; }
    public void setShadowRadius(int shadowRadius) { this.shadowRadius = Math.max(0, shadowRadius); }

    public int getGlowRadius() { return glowRadius; }
    public void setGlowRadius(int glowRadius) { this.glowRadius = Math.max(0, glowRadius); }

    public float getOpacity() { return opacity; }
    public void setOpacity(float opacity) { this.opacity = Math.clamp(opacity, 0f, 1f); }

    public float getFontSize() { return fontSize; }
    public void setFontSize(float fontSize) { this.fontSize = Math.max(0.5f, fontSize); }

    public String getFontFamily() { return fontFamily; }
    public void setFontFamily(String fontFamily) { this.fontFamily = fontFamily; }

    public int getPadding() { return padding; }
    public void setPadding(int padding) { this.padding = Math.max(0, padding); }

    public int getMargin() { return margin; }
    public void setMargin(int margin) { this.margin = Math.max(0, margin); }

    public int getSpacing() { return spacing; }
    public void setSpacing(int spacing) { this.spacing = Math.max(0, spacing); }

    public float getIconScale() { return iconScale; }
    public void setIconScale(float iconScale) { this.iconScale = Math.max(0.2f, iconScale); }

    public AnimationType getAnimationType() { return animationType; }
    public void setAnimationType(AnimationType animationType) { this.animationType = animationType; }
}
