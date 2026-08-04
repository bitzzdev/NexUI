package com.nexui.model;

/**
 * Interface Theme definition for global layout styling presets.
 */
public class Theme {
    private final String id;
    private String name;
    private String description;
    private ColorRGBA primaryColor;
    private ColorRGBA secondaryColor;
    private ColorRGBA backgroundColor;
    private ColorRGBA accentColor;
    private ColorRGBA textColor;
    private int defaultRadius;
    private int defaultBlur;
    private boolean isBuiltIn;

    public Theme(String id, String name, String description, ColorRGBA primaryColor,
                 ColorRGBA secondaryColor, ColorRGBA backgroundColor, ColorRGBA accentColor,
                 ColorRGBA textColor, int defaultRadius, int defaultBlur, boolean isBuiltIn) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.primaryColor = primaryColor;
        this.secondaryColor = secondaryColor;
        this.backgroundColor = backgroundColor;
        this.accentColor = accentColor;
        this.textColor = textColor;
        this.defaultRadius = defaultRadius;
        this.defaultBlur = defaultBlur;
        this.isBuiltIn = isBuiltIn;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public ColorRGBA getPrimaryColor() { return primaryColor; }
    public void setPrimaryColor(ColorRGBA primaryColor) { this.primaryColor = primaryColor; }

    public ColorRGBA getSecondaryColor() { return secondaryColor; }
    public void setSecondaryColor(ColorRGBA secondaryColor) { this.secondaryColor = secondaryColor; }

    public ColorRGBA getBackgroundColor() { return backgroundColor; }
    public void setBackgroundColor(ColorRGBA backgroundColor) { this.backgroundColor = backgroundColor; }

    public ColorRGBA getAccentColor() { return accentColor; }
    public void setAccentColor(ColorRGBA accentColor) { this.accentColor = accentColor; }

    public ColorRGBA getTextColor() { return textColor; }
    public void setTextColor(ColorRGBA textColor) { this.textColor = textColor; }

    public int getDefaultRadius() { return defaultRadius; }
    public void setDefaultRadius(int defaultRadius) { this.defaultRadius = defaultRadius; }

    public int getDefaultBlur() { return defaultBlur; }
    public void setDefaultBlur(int defaultBlur) { this.defaultBlur = defaultBlur; }

    public boolean isBuiltIn() { return isBuiltIn; }
}
