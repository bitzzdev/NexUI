# NexUI - Modern Minecraft Interface Studio

![NexUI Banner](https://img.shields.io/badge/Minecraft-26.2-green?style=for-the-badge&logo=minecraft)
![Fabric](https://img.shields.io/badge/Fabric-0.19.3-blue?style=for-the-badge&logo=fabric)
![Java](https://img.shields.io/badge/Java-25%2B-orange?style=for-the-badge&logo=openjdk)
![License](https://img.shields.io/badge/License-MIT-purple?style=for-the-badge)

**NexUI** is a modern, client-side Fabric mod for **Minecraft 26.2** that completely reimagines how players interact with Minecraft's interface. 

Designed like Figma, NexUI gives players total freedom to reposition, resize, animate, and theme every HUD and GUI element in real-time with zero performance overhead and full multiplayer compatibility.

---

## Features

### 🎨 Design Mode (HotKey: `Right Shift`)
- **Interactive Canvas**: Drag, drop, scale, rotate, snap-to-grid, and smart alignment guides.
- **Multi-Select & Locking**: Select multiple UI components, lock critical elements, duplicate, and copy/paste styling between components.
- **Undo / Redo History**: Full state history stack so you can experiment without fear.
- **Property Inspector**: Right-hand panel displaying exact pixel coordinates, bounds, border radius, blur, shadow, glow, opacity, and animation curves.

### 🎭 Unlimited Themes
Built-in preset themes with instant switching:
- **Vanilla**: Classic Minecraft interface look.
- **Minimalist**: Clean borderless dark mode.
- **Modern Studio**: Vibrant indigo accents & glass contrast.
- **AMOLED**: True-black high contrast theme.
- **Frosted Glass**: Translucent glassmorphism with blur effects.
- **RPG Fantasy**: Warm parchment tones with gold accents.
- **Synthwave Neon**: Cyan & magenta neon aesthetics.
- **Cyberpunk 2077**: High-contrast yellow & obsidian dark UI.
- **Competitive PvP**: Compact HUD maximizing field of view.
- **Streamer Broadcast**: Distinct borders for facecam screen capture overlays.

### 📐 Layout Profiles
Switch layouts instantly for different playstyles:
- `Survival Default`
- `Competitive PvP`
- `Architect & Building`
- `Streamer Broadcast`
- `Speedrun Timer & Stats`
- `Accessibility High-Contrast`

### 🔌 Extensible Public API
Third-party Fabric mods can register custom HUD widgets and GUI components via `NexUIAPI`:
```java
// Register a custom HUD widget
NexUIAPI.registerWidget(new MyCustomModWidget());

// Register a custom theme
NexUIAPI.registerTheme(new MyCustomTheme());
```

---

## Technical Stack & Architecture

- **Minecraft Target**: 26.2
- **Fabric Loader**: 0.19.3
- **Fabric Loom**: 1.17
- **Java**: 25+
- **Architecture**: Modular MVVM-inspired design split into `model`, `api`, `registry`, `engine`, `config`, `ui`, and `integration`.
- **CI / Build Workflow**: Automated GitHub Actions build pipeline uploading release JAR artifacts.

---

## Building from Source

NexUI targets **Minecraft 26.2**, a non-obfuscated release. Because of this, Loom skips remapping: dependencies are declared with plain `implementation` (not `modImplementation`) and the output is a normal `jar` (not `remapJar`).

Prerequisites:
- **JDK 25** (Minecraft 26.2 requires Java 25 / `java-runtime-epsilon`)
- **Gradle 9.5** (bundled via the wrapper; required by Fabric Loom 1.17)

```bash
# Build the mod JAR (no remapping needed for 26.2)
./gradlew build --no-daemon
```

The artifact is written to `build/libs/nexui-<version>.jar`. The build is also run automatically by the GitHub Actions workflow in `.github/workflows/build.yml` on every push; the built JAR is attached as a workflow artifact.

> Note: Fabric Loom never runs its download/remap tasks on a plain non-obfuscated 26.2 project, but it does download the Minecraft client and Fabric API from Maven on first build.

---

## License

Distributed under the MIT License.
