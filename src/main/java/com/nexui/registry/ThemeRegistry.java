package com.nexui.registry;

import com.nexui.model.ColorRGBA;
import com.nexui.model.Theme;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Registry containing built-in themes and user-created custom themes.
 */
public class ThemeRegistry {
    private static final ThemeRegistry INSTANCE = new ThemeRegistry();
    private final Map<String, Theme> themes = new LinkedHashMap<>();

    private ThemeRegistry() {
        registerBuiltInThemes();
    }

    public static ThemeRegistry getInstance() {
        return INSTANCE;
    }

    private void registerBuiltInThemes() {
        registerTheme(new Theme(
            "vanilla", "Vanilla", "Classic Minecraft interface appearance",
            new ColorRGBA(196, 196, 196, 255), new ColorRGBA(139, 139, 139, 255),
            new ColorRGBA(0, 0, 0, 160), new ColorRGBA(255, 255, 160, 255),
            ColorRGBA.WHITE, 0, 0, true
        ));

        registerTheme(new Theme(
            "minimal", "Minimalist", "Ultra clean, borderless dark interface",
            new ColorRGBA(35, 39, 47, 240), new ColorRGBA(48, 54, 64, 240),
            new ColorRGBA(18, 20, 24, 200), new ColorRGBA(99, 102, 241, 255),
            new ColorRGBA(240, 242, 245, 255), 4, 2, true
        ));

        registerTheme(new Theme(
            "modern", "Modern Studio", "Clean Figma-inspired interface with vibrant indigo accents",
            new ColorRGBA(24, 24, 32, 220), new ColorRGBA(36, 36, 48, 240),
            new ColorRGBA(14, 14, 20, 220), ColorRGBA.ACCENT_BLUE,
            ColorRGBA.WHITE, 8, 4, true
        ));

        registerTheme(new Theme(
            "amoled", "AMOLED Pitch Dark", "Deep true-black theme optimized for high contrast",
            new ColorRGBA(0, 0, 0, 255), new ColorRGBA(15, 15, 15, 255),
            new ColorRGBA(0, 0, 0, 230), ColorRGBA.ACCENT_CYAN,
            ColorRGBA.WHITE, 6, 0, true
        ));

        registerTheme(new Theme(
            "glass", "Frosted Glassmorphism", "Translucent glass effect with soft blur and glows",
            new ColorRGBA(255, 255, 255, 30), new ColorRGBA(255, 255, 255, 50),
            new ColorRGBA(20, 20, 30, 140), new ColorRGBA(147, 197, 253, 255),
            ColorRGBA.WHITE, 12, 10, true
        ));

        registerTheme(new Theme(
            "rpg", "RPG Fantasy", "Warm parchment tone with golden borders and high readability",
            new ColorRGBA(52, 38, 26, 230), new ColorRGBA(74, 55, 38, 240),
            new ColorRGBA(32, 22, 14, 210), new ColorRGBA(234, 179, 8, 255),
            new ColorRGBA(254, 243, 199, 255), 6, 0, true
        ));

        registerTheme(new Theme(
            "neon", "Synthwave Neon", "Vibrant magenta and cyan neon aesthetics",
            new ColorRGBA(26, 12, 38, 230), new ColorRGBA(45, 18, 66, 240),
            new ColorRGBA(15, 6, 24, 220), new ColorRGBA(236, 72, 153, 255),
            new ColorRGBA(244, 244, 245, 255), 8, 6, true
        ));

        registerTheme(new Theme(
            "cyber", "Cyberpunk 2077", "High-contrast yellow and obsidian dark UI",
            new ColorRGBA(254, 240, 138, 240), new ColorRGBA(24, 24, 27, 240),
            new ColorRGBA(12, 12, 16, 230), new ColorRGBA(250, 204, 21, 255),
            new ColorRGBA(255, 255, 255, 255), 2, 0, true
        ));

        registerTheme(new Theme(
            "pvp", "Competitive PvP", "Ultra compact HUD with maximum field of view transparency",
            new ColorRGBA(15, 23, 42, 160), new ColorRGBA(30, 41, 59, 180),
            new ColorRGBA(2, 6, 23, 120), new ColorRGBA(239, 68, 68, 255),
            ColorRGBA.WHITE, 4, 0, true
        ));

        registerTheme(new Theme(
            "streamer", "Streamer Broadcast", "Clean high-contrast borders for OBS screen capture",
            new ColorRGBA(30, 27, 75, 230), new ColorRGBA(49, 46, 129, 240),
            new ColorRGBA(15, 12, 41, 220), new ColorRGBA(168, 85, 247, 255),
            ColorRGBA.WHITE, 10, 4, true
        ));
    }

    public void registerTheme(Theme theme) {
        if (theme != null) {
            themes.put(theme.getId(), theme);
        }
    }

    public Theme getTheme(String id) {
        return themes.getOrDefault(id, themes.get("modern"));
    }

    public List<Theme> getAllThemes() {
        return new ArrayList<>(themes.values());
    }
}
