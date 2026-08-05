package com.nexui.model;

/**
 * 2D Integer Bounding Rectangle with intersection, containment, and resizing helper methods.
 */
public record Rect2i(int x, int y, int width, int height) {

    public boolean contains(int px, int py) {
        return px >= x && px <= x + width && py >= y && py <= y + height;
    }

    public boolean intersects(Rect2i other) {
        return this.x < other.x + other.width &&
               this.x + this.width > other.x &&
               this.y < other.y + other.height &&
               this.y + this.height > other.y;
    }

    public Rect2i translate(int dx, int dy) {
        return new Rect2i(this.x + dx, this.y + dy, this.width, this.height);
    }

    public Rect2i withSize(int newWidth, int newHeight) {
        return new Rect2i(this.x, this.y, Math.max(10, newWidth), Math.max(10, newHeight));
    }

    public int centerX() {
        return x + width / 2;
    }

    public int centerY() {
        return y + height / 2;
    }

    public int right() {
        return x + width;
    }

    public int bottom() {
        return y + height;
    }
}
