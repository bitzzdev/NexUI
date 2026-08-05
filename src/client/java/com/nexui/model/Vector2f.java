package com.nexui.model;

/**
 * Immutable 2D Float Vector for positions, scale, and offsets.
 */
public record Vector2f(float x, float y) {
    public static final Vector2f ZERO = new Vector2f(0f, 0f);
    public static final Vector2f ONE = new Vector2f(1f, 1f);

    public Vector2f add(float dx, float dy) {
        return new Vector2f(this.x + dx, this.y + dy);
    }

    public Vector2f add(Vector2f other) {
        return new Vector2f(this.x + other.x, this.y + other.y);
    }

    public Vector2f multiply(float factor) {
        return new Vector2f(this.x * factor, this.y * factor);
    }

    public Vector2f clamp(float minX, float minY, float maxX, float maxY) {
        return new Vector2f(
            Math.max(minX, Math.min(maxX, this.x)),
            Math.max(minY, Math.min(maxY, this.y))
        );
    }
}
