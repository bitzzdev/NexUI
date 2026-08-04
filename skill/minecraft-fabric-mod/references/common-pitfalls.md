# Common pitfalls when building Fabric mods for new Minecraft versions

Check this list before telling the user the mod is done.

1. **Mismatched Yarn mappings build.** The most common single point of failure. A mappings
   string like `1.21.9+build.1` must exactly match a build that actually exists for that MC
   version on Fabric's maven. Always confirm via `https://meta.fabricmc.net/v2/versions/yarn/<MC_VERSION>`
   rather than incrementing a previous version's build number by guesswork.

2. **Fabric API version doesn't support the target MC version.** Fabric API ships a
   different build per MC version (or per small range of versions) — a Fabric API version
   built for an older MC version will fail to resolve or will resolve but crash at runtime.
   Cross-check on https://github.com/FabricMC/fabric/releases or Modrinth's Fabric API page,
   filtered to the exact target MC version.

3. **Wrong Loom plugin ID.** Starting mid-2026, Fabric Loom has separate plugin ids:
   `net.fabricmc.fabric-loom` for non-obfuscated Minecraft versions and
   `net.fabricmc.fabric-loom-remap` for obfuscated versions (the legacy `fabric-loom` id
   still works for backward compatibility). Using the wrong one for a very new MC version
   can silently misconfigure remapping. Check the Loom release notes for the target version.

4. **Gradle version too old for the chosen Loom version.** Loom releases bump their minimum
   required Gradle version fairly often. If `./gradlew build` fails immediately with a
   Gradle-compatibility error, check the Loom changelog for the stated minimum and update
   `gradle/wrapper/gradle-wrapper.properties`.

5. **Wrong Java version.** Recent Minecraft versions require newer JDKs (Java 21 as of the
   1.20.5+ era). If `sourceCompatibility`/`targetCompatibility` in `build.gradle` don't match
   what the target MC version needs, compilation fails or the game refuses to launch.

6. **Registry API changes.** Minecraft's registration APIs (for items, blocks, blockstates,
   creative tabs, etc) have changed meaningfully across versions — deferred/registry-key
   based registration replaced some older direct-registration patterns in various updates.
   Don't assume registration code from an older MC version's mod compiles unchanged against
   a newer one — search for `<MC_VERSION> fabric how to register item` (or block/etc) if the
   target version postdates your training knowledge, rather than copying an old pattern.

7. **Mixin refmap / mixin config mistakes.** If mixins are used, `mymod.mixins.json` must
   correctly list the mixin package and the `build.gradle`/Loom setup must be configured to
   produce a refmap; a missing refmap causes runtime `MixinTransformationException`s. Only
   add mixins if the mod actually needs to alter vanilla behavior that can't be done via
   normal Fabric API hooks — most simple mods (new items/blocks/recipes) need zero mixins.

8. **Client-only code placed in the main sourceset.** Rendering code, screen/GUI code, and
   keybind registration are client-only. If not using split sourcesets, guard such code so
   it's never referenced from code that also runs on a dedicated server, or the mod will
   crash dedicated servers with a `ClassNotFoundException` for client-only Minecraft classes.

9. **Assuming an unreleased/snapshot MC version is "the latest release."** If a user says
   "the new version," confirm whether they mean the latest *stable release* or a snapshot —
   Fabric support for snapshots can lag, and mappings/Fabric API for snapshots are less
   stable. Ask if ambiguous, or default to latest stable and say so.

10. **Not verifying at all.** If Gradle/network access is available in the environment,
    actually attempt a build (`./gradlew build` or at minimum `./gradlew tasks`) rather than
    only generating files and declaring success. If it's not available, say so explicitly
    so the user knows to verify locally.
