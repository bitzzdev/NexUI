package com.nexui.integration;

import com.nexui.model.Rect2i;

/**
 * Computes where vanilla actually renders each HUD element for a given scaled
 * window size. NexUI's stored bounds are only meaningful when expressed
 * relative to these runtime anchors, so relocator boxes always overlay the real
 * elements regardless of window resolution.
 */
public final class VanillaHudGeometry {

    private VanillaHudGeometry() {
    }

    /**
     * Returns the vanilla render rectangle for the given HUD component at the
     * current scaled window size, or {@code null} for non-HUD (container) ids
     * which are positioned by absolute coordinates instead.
     */
    public static Rect2i anchorFor(String componentId, int scaledWidth, int scaledHeight) {
        int w = scaledWidth;
        int h = scaledHeight;
        switch (componentId) {
            case "hotbar":
                return new Rect2i(w / 2 - 91, h - 22, 182, 22);
            case "crosshair":
                return new Rect2i((w - 15) / 2, (h - 15) / 2, 15, 15);
            case "xp_bar":
                return new Rect2i((w - 182) / 2, h - 29, 182, 5);
            case "xp_level":
                return new Rect2i(w / 2 - 25, h - 35, 50, 9);
            case "hearts":
                return new Rect2i(w / 2 - 91, h - 39, 80, 9);
            case "armor":
                return new Rect2i(w / 2 - 91, h - 49, 80, 9);
            case "hunger":
                return new Rect2i(w / 2 + 10, h - 39, 72, 9);
            case "air_bar":
                return new Rect2i(w / 2 + 10, h - 49, 72, 9);
            case "mount_hud":
                return new Rect2i(w / 2 + 10, h - 39, 72, 9);
            case "action_bar":
                return new Rect2i(w / 2 - 100, h - 72, 200, 9);
            case "chat":
                return new Rect2i(2, h - 120, 320, 100);
            case "scoreboard":
                return new Rect2i(Math.max(0, w - 160), 10, 150, 100);
            case "titles":
                return new Rect2i(w / 2 - 100, h / 2 - 60, 200, 60);
            case "boss_bars":
                return new Rect2i(w / 2 - 91, 12, 182, 20);
            default:
                return null;
        }
    }

    /**
     * Returns a rectangle of the given size centered on the screen (used as the
     * vanilla anchor for container screens).
     */
    public static Rect2i centeredAnchor(int scaledWidth, int scaledHeight, Rect2i size) {
        return new Rect2i((scaledWidth - size.width()) / 2, (scaledHeight - size.height()) / 2, size.width(), size.height());
    }
}
