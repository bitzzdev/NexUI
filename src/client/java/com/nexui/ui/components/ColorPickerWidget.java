package com.nexui.ui.components;

import com.nexui.model.ColorRGBA;

/**
 * Custom color picker widget data model.
 */
public class ColorPickerWidget {
    private ColorRGBA selectedColor;

    public ColorPickerWidget(ColorRGBA initialColor) {
        this.selectedColor = initialColor != null ? initialColor : ColorRGBA.WHITE;
    }

    public ColorRGBA getSelectedColor() {
        return selectedColor;
    }

    public void setSelectedColor(ColorRGBA selectedColor) {
        this.selectedColor = selectedColor;
    }
}
