# Changelog

All notable changes to NexUI are documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/), and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [0.0.2-alpha] - 2026-08-08

### Added

- **Layout persistence** — relocator positions now save to `config/nexui.json` and are restored on the next launch, so your layout survives game restarts. Offsets are window-size independent, so the layout holds even if you change resolution between sessions.
- **Reset to vanilla keybind** (`U` by default, rebindable under Controls → NexUI Studio) — instantly snaps every element back to its original vanilla position and clears all offsets, visibility and scale changes.
- `UIComponent.setDefaultBounds` setter.

### Fixed

- **Relocator boxes now sit exactly on the real HUD elements.** Previously the boxes were drawn at fixed hardcoded positions that drifted from the actual render positions (the crosshair box was offset to the right, and the hotbar box could render below the canvas on short windows). Boxes are now anchored to vanilla's real render math every time the studio opens or the window resizes.

## [0.0.1-alpha] - Initial Release

### Added

- **Design Studio** — open an interactive Figma-style overlay over your live game (default `Right Shift`) and drag the real HUD/GUI elements into new positions in real time.
- **Real element relocation** — NexUI wraps the actual vanilla HUD layers and container screens, so moving a relocator box moves the real element in-game (multiplayer-safe, client-side only).
- **Relocatable elements** — hotbar, health/armor/food/air/mount bars, XP bar and level, crosshair, action bar, chat, scoreboard, titles, boss bars, and container screens (inventory, chest, furnace, crafting table, anvil, brewing stand, enchanting table, beacon, smithing table, loom, stonecutter, merchant).
- **Grid snap & smart alignment guides** — snapping to a pixel grid and live alignment guides.
- **Multi-select drag** — move many relocators together while keeping their relative spacing.
- **Locking, undo/redo, copy/paste style** — 50-step history and per-element locking, style copy/paste.
- **Property inspector** — right-hand panel with exact pixel coordinates, bounds, and visual properties of the selected element.
- **Element visibility screen** — toggle relocators ON/OFF (only removes the box from the canvas, never hides the real UI).
- **Themes** — Vanilla, Minimalist, Modern Studio, AMOLED, Frosted Glass, RPG Fantasy, Synthwave Neon, Cyberpunk 2077, Competitive PvP, and Streamer Broadcast.
- **Developer API** — register custom widgets and styles through the public API.
- **ModMenu integration** — NexUI appears in the ModMenu mod list.

[0.0.2-alpha]: https://github.com/bitzzdev/NexUI/releases/tag/0.0.2-alpha
[0.0.1-alpha]: https://github.com/bitzzdev/NexUI/releases/tag/0.0.1-alpha
