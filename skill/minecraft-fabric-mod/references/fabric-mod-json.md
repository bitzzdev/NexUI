# fabric.mod.json template

Located at `src/main/resources/fabric.mod.json`. This is Fabric's mod metadata/manifest file.

```json
{
  "schemaVersion": 1,
  "id": "mymod",
  "version": "${version}",
  "name": "My Mod",
  "description": "A short description of what the mod does.",
  "authors": [
    "YourName"
  ],
  "contact": {
    "homepage": "https://example.com/",
    "sources": "https://github.com/you/mymod"
  },
  "license": "MIT",
  "icon": "assets/mymod/icon.png",
  "environment": "*",
  "entrypoints": {
    "main": [
      "com.example.mymod.MyMod"
    ],
    "client": [
      "com.example.mymod.MyModClient"
    ]
  },
  "mixins": [
    "mymod.mixins.json"
  ],
  "depends": {
    "fabricloader": ">=<LOADER_VERSION>",
    "minecraft": "~<MC_VERSION>",
    "java": ">=21",
    "fabric-api": "*"
  }
}
```

## Field notes

- `id` — lowercase, no spaces, matches the mod's internal identifier used elsewhere (package name, etc).
- `"environment": "*"` means the mod runs on both client and server. Use `"client"` for
  client-only mods, `"server"` for server-only.
- `entrypoints.client` is only needed if you split out client-only code (see
  `build-gradle-template.md` notes on `splitEnvironmentSourceSets`). Remove it if the mod
  has no client-only entrypoint class.
- `mixins` — only include this array, and only reference a `.mixins.json` file, if the mod
  actually uses Mixin. Don't scaffold an empty mixins file if it's not needed.
- `depends.minecraft` — use `~<MC_VERSION>` (tilde, matches patch-version range) or an exact
  string depending on how strict you want compatibility to be; confirm the convention against
  the current Fabric docs if unsure, conventions have shifted over time.
- `depends.fabricloader` should reference the loader version confirmed during research, not guessed.
