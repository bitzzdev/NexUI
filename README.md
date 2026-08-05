<div align="center">
  <img src="https://raw.githubusercontent.com/bitzzdev/NexUI/main/logo.png" width="128" alt="NexUI logo"/>
  <h1>NexUI — Interface Studio</h1>
  <p><b>Design, move, theme, and fine-tune every HUD and GUI element of Minecraft, live — Figma-style.</b></p>

  ![Fabric](https://img.shields.io/badge/Mod%20Loader-Fabric-lightgrey?style=for-the-badge&logo=modrinth)
  ![Environment](https://img.shields.io/badge/Environment-Client-only-blue?style=for-the-badge)
  ![License](https://img.shields.io/badge/License-MIT-purple?style=for-the-badge)
</div>

---

**NexUI** is a client-side Fabric mod that completely reimagines how you interact with Minecraft's interface. It works like a design tool (think Figma): you open a studio overlay over your live game and **drag the real UI elements** into new positions, hide relocators you don't need, switch complete layouts, and apply whole themes — in real time, with zero performance overhead and full multiplayer compatibility.

The key difference from other HUD mods: NexUI doesn't draw fake boxes and guess. It wraps the actual vanilla HUD layers and container screens, so when you move the *hotbar* box on the canvas, the **real hotbar moves in your world**.

---

## Table of Contents

- [Features](#features)
- [Installation](#installation)
- [Quick Start](#quick-start)
- [Keybinds & Controls](#keybinds--controls)
- [Relocatable Elements](#relocatable-elements)
- [Layout Profiles](#layout-profiles)
- [Themes](#themes)
- [Element Visibility](#element-visibility)
- [Compatibility](#compatibility)
- [Developer API](#developer-api)
- [Building from Source](#building-from-source)
- [License](#license)

---

## Features

### 🎨 Design Studio (Design Mode)
Press the **Design Mode key** (default `Right Shift`) to open an interactive studio overlay rendered right on top of your live game:

- **Drag & drop relocation** — grab a relocator and move it; the real HUD/screen element follows 1:1 with smooth cursor tracking.
- **Grid snap & smart alignment guides** — toggle snapping to a pixel grid and get live guide lines that line elements up with each other and the screen edges.
- **Multi-select** — click to select, then drag many relocators together while keeping their relative spacing.
- **Locking** — lock critical elements so they can't be moved by accident.
- **Undo / Redo** — full 50-step history stack, so you can experiment freely.
- **Copy / Paste style** — copy the visual style of one element and apply it to others.
- **Property inspector** — a right-hand panel with exact pixel coordinates, bounds, and every visual property of the selected element.
- **Real-time preview** — the actual game keeps rendering behind the studio, and the changes apply to the real UI the moment you move a box.

### 📐 Layout Profiles
Profiles are named, switchable layouts. Save a setup for survival, one for PvP, one for streaming — and flip between them instantly.

### 🎭 Themes
Themes are full visual style kits applied across the HUD. The built-in presets include Vanilla, Minimalist, Modern Studio, AMOLED, Frosted Glass, RPG Fantasy, Synthwave Neon, Cyberpunk 2077, Competitive PvP, and Streamer Broadcast.

### 👁 Element Visibility
A dedicated screen lists every relocator with an ON/OFF pill. Toggling an element **only removes its relocator from the design canvas** — it never hides the real in-game UI, so you can declutter the canvas safely without breaking your layout.

### 🔌 Extensible Public API
Third-party mods can register custom widgets, widget providers, and themes through `NexUIAPI`.

---

## Installation

1. Install [Fabric Loader](https://fabricmc.net/) and [Fabric API](https://modrinth.com/mod/fabric-api) for your Minecraft version.
2. Drop the NexUI `.jar` into your `mods/` folder.
3. Launch the game. The NexUI category appears under **Options → Controls**, and a **"Configure…"** button appears in [Mod Menu](https://modrinth.com/mod/modmenu) if you have it installed.

Requirements: a recent Minecraft release, a recent Java, Fabric Loader, and Fabric API. Mod Menu is optional.

---

## Quick Start

1. Launch the game and join any world or server.
2. Press `Right Shift` to open the Design Studio.
3. Click a relocator box to select it, drag it to a new spot — the **real** UI element moves with it.
4. Press `Esc` to save and exit. The new placement persists immediately.
5. Open the **Visibility** screen (toolbar button) to hide relocators you don't want on the canvas.
6. Use the **Profile** and **Theme** screens to switch or save complete setups.

---

## Keybinds & Controls

| Input | Action |
| --- | --- |
| `Right Shift` | Open / close the Design Studio (rebindable in Controls) |
| `Esc` | Save & exit the studio |
| Left click | Select a relocator |
| Drag | Move the selected relocator(s) |
| `L` | Lock / unlock the selected relocator |
| `Ctrl+Z` / `Ctrl+Y` | Undo / redo |
| `Ctrl+C` / `Ctrl+V` | Copy / paste style |
| Click a row | Toggle that relocator's visibility (ON/OFF) |
| Show All / Hide All | Set visibility for every relocator at once |

---

## Relocatable Elements

Every element below is a **relocator**: a box on the design canvas that is bound to the real vanilla UI element, not a placeholder.

**HUD layers**

| Relocator | Real element |
| --- | --- |
| Hotbar | Item hotbar |
| Health Hearts | Player health hearts |
| Hunger Bar | Food/saturation bar |
| Armor Bar | Armor bar |
| Experience Bar | XP progress bar |
| XP Level Indicator | Level number |
| Crosshair | Center crosshair |
| Air Bubble Bar | Underwater air bubbles |
| Mount Jump / Health | Mount health & jump bar |
| Action Bar Text | Status/action messages |
| Chat Window | Chat (confirmed working) |
| Scoreboard Sidebar | Sidebar scoreboard |
| Title & Subtitle | Screen titles |
| Boss Health Bars | Boss bars |

**Container screens** (moved where the box sits — WYSIWYG)

| Relocator | Real screen |
| --- | --- |
| Inventory Screen | Player inventory |
| Chest Screen | Chest / shulker boxes / dispensers / hoppers |
| Furnace Screen | Furnace / blast furnace / smoker |
| Crafting Table Screen | Crafting table |
| Anvil Screen | Anvil (confirmed working) |
| Brewing Stand Screen | Brewing stand |
| Enchanting Table Screen | Enchanting table |
| Beacon Screen | Beacon |
| Smithing Table Screen | Smithing table |
| Loom Screen | Loom |
| Stonecutter Screen | Stonecutter |
| Villager Merchant Screen | Trading |

---

## Layout Profiles

Profiles are stored independently of themes, so you can pair any profile with any theme.

- **Survival Default** — the vanilla-centered layout.
- **Competitive PvP** — compact HUD maximizing field of view.
- **Architect & Building** — chat and hotbar cleared out of the build area.
- **Streamer Broadcast** — HUD arranged away from a corner facecam.
- **Speedrun Timer & Stats** — layout tuned for timer overlays.
- **Accessibility High-Contrast** — large, high-visibility elements.

Switch profiles at any time from the Profile Manager; your current edits are saved to the active profile automatically.

---

## Themes

| Theme | Look |
| --- | --- |
| Vanilla | Classic Minecraft interface |
| Minimalist | Clean, borderless dark mode |
| Modern Studio | Vibrant indigo accents & glass contrast |
| AMOLED | True-black, high contrast |
| Frosted Glass | Translucent glassmorphism with blur |
| RPG Fantasy | Warm parchment tones with gold accents |
| Synthwave Neon | Cyan & magenta neon |
| Cyberpunk 2077 | High-contrast yellow & obsidian |
| Competitive PvP | Compact, distraction-free |
| Streamer Broadcast | Distinct borders for facecam capture |

Themes apply per-style properties (background, border, shadow, blur, glow, opacity) across HUD components and are designed to be instantly switchable without touching your layout.

---

## Element Visibility

Open the **Visibility** screen from the studio toolbar (or the listed button) to see every relocator with an ON/OFF state:

- **Click a row** to toggle that relocator on/off.
- **Show All / Hide All** applies the choice to every relocator at once.
- A counter shows how many relocators are currently visible.

Toggling a relocator off **only removes it from the design canvas**. The real HUD element and container screens are never hidden by this option, so you can't accidentally lose your hotbar mid-play.

---

## Compatibility

NexUI is a **client-only** mod and works on multiplayer servers (it never modifies the network or server state). It includes compatibility adapters for popular rendering and QoL mods (Sodium, Iris, Lithium, EMI, JEI) and a first-class Mod Menu integration.

---

## Developer API

Fabric mods can register custom widgets and themes from their `onInitializeClient`:

```java
// Register a custom HUD widget
NexUIAPI.registerWidget(new MyCustomModWidget());

// Register a widget provider (batch registration)
NexUIAPI.registerProvider(myProvider);

// Register a custom theme
NexUIAPI.registerTheme(new MyCustomTheme());
```

Implement `NexUIWidget` to describe your widget (id, name, category, default bounds, render callback, availability) and `WidgetProvider` to provide several widgets at once. Registered widgets appear in the studio like any vanilla element and can be moved, themed, and included in profiles.

The `com.nexui.api` package is the stable public surface; everything else (`model`, `registry`, `engine`, `config`, `ui`, `integration`, `mixin`) is internal and may change between versions.

---

## Building from Source

NexUI targets recent **non-obfuscated** Minecraft releases. This changes the build in two ways:

- Loom skips remapping, so dependencies use plain `implementation` and the output is a normal `jar` (no `remapJar`).
- The Minecraft client and Fabric API are downloaded from Maven on the first build.

Prerequisites:

- A recent **JDK** (match the Java version required by the target Minecraft release).
- A recent **Gradle** (the wrapper bundles the version the project uses).

```bash
./gradlew build --no-daemon
```

The artifact is written to `build/libs/`. A GitHub Actions workflow also builds on every push and attaches the JAR as a workflow artifact.

---

## License

Distributed under the [MIT License](LICENSE).
