---
name: minecraft-fabric-mod
description: Use whenever the user asks to build, scaffold, create, or update a Minecraft Java Edition mod using Fabric (Gradle, Fabric Loom, Fabric API, Yarn mappings). Always use if the user names a Minecraft version, especially a recent one released after training data cutoff — do not rely on memorized version numbers, mappings, or loader/API versions, since these are frequently wrong for anything released recently. Trigger on phrases like "make a minecraft mod", "fabric mod for 1.X", "build a mod for the new version", "add an item/block/command to my mod", or requests touching build.gradle, fabric.mod.json, gradle.properties, or src/main/java mod entrypoints in a Fabric project. Enforces mandatory web-research before writing Gradle/version config, and requires every project to build exclusively through a GitHub Actions CI workflow rather than local Gradle, since guessing versions or relying on local builds produces mods that fail silently or can't be verified.
---

# Minecraft Fabric Mod Builder

## Core principle: never guess versions

The single biggest failure mode for AI-built Fabric mods is **hallucinated version numbers** — a plausible-looking but wrong Minecraft version, Yarn mappings build, Fabric Loader version, Fabric API version, or Loom plugin version. These numbers change constantly and the agent's training data is always stale relative to "the new version" a user mentions. A mod with even one wrong version string in `gradle.properties` will fail to sync or build.

**Therefore: before writing or editing any `gradle.properties`, `build.gradle`, or `settings.gradle` for a Fabric project, the agent MUST look up current values instead of recalling them from memory.** This is not optional and applies even if the agent feels confident it knows the numbers.

## Core rule: when an error or bug appears, prefer documentation over guessing

Whenever a Gradle build fails, a compile/runtime error appears, a Minecraft API behaves
unexpectedly, or a bug shows up in mod code (fresh or existing), **do not fix it from memory or
by guessing** what is "probably" wrong. Fabric (loader, Loom, Fabric API, Yarn mappings) and
Minecraft itself change APIs and mappings faster than training data can stay current — a
confident-sounding guess is exactly what produces a second, silent failure.

When an error/bug appears:
1. **Get the exact error text** — the full build/compile/stack trace message from the CI log,
   not a paraphrase. Ask the user for the precise error output if it is missing or ambiguous.
2. **Search first** — run a web search or fetch official docs using the exact error string and
   the MC/Fabric version involved. Prefer authoritative sources: `https://docs.fabricmc.net`,
   `https://meta.fabricmc.net` (the version APIs), `https://github.com/FabricMC/fabric-loom/releases`,
   `https://github.com/FabricMC/fabric/releases`, the changelogs of the relevant library, and
   linked issue/discussion threads that quote official docs.
3. **Apply a fix only after confirming the cause against documentation.** If you cannot find
   authoritative docs or confirm the correct behavior, say so and mark the fix as unverified
   rather than presenting a guess as a solution.

## Step 0 — Figure out the target Minecraft version

- If the user names a specific version (e.g. "1.21.9", "the new version", "the latest release"), that's the target.
- If they say "the latest" or "the new version" without a number, search to find out what the actual current stable release is first — don't assume.

## Step 1 — Mandatory research (do this every time, even for versions you think you know)

Run web searches for each of these, in order. Use the actual target MC version in the query.

1. **Minecraft version confirmation** — search `minecraft java edition latest version` (only if the user didn't give an exact number) to confirm the real current release and its exact string (e.g. `1.21.9`, not `1.21` or `1.22`).
2. **Fabric Loader version** — search `fabric loader latest version` or check https://meta.fabricmc.net/v2/versions/loader (this API returns JSON directly, fetch it with web_fetch — it's the authoritative machine-readable source, always prefer it over prose).
3. **Yarn mappings for the target MC version** — search `yarn mappings <MC_VERSION>` or fetch `https://meta.fabricmc.net/v2/versions/yarn/<MC_VERSION>` — this tells you the exact mappings build number (e.g. `1.21.9+build.1`). This is the number people get wrong most often.
4. **Fabric API version compatible with the target MC version** — search `fabric api <MC_VERSION> download` and check https://github.com/FabricMC/fabric/releases or Modrinth/CurseForge for the specific build tagged for that MC version. Fabric API versions do NOT map 1:1 across MC versions — always confirm the exact one for the target version.
5. **Fabric Loom version** — search `fabric loom latest gradle plugin version` or check https://github.com/FabricMC/fabric-loom/releases. Also note: as of mid-2026, Loom introduced two plugin IDs — `net.fabricmc.fabric-loom` (non-obfuscated MC versions) vs `net.fabricmc.fabric-loom-remap` (obfuscated versions) — confirm which applies to the target MC version, and check the minimum required Gradle version for that Loom release.
6. **Gradle wrapper version** — confirm the Gradle version required by the Loom release found above (Loom releases usually state a minimum Gradle version in their changelog).
7. **Java version required** — search `minecraft <MC_VERSION> java version required` — recent Minecraft versions have bumped minimum Java versions (e.g. Java 21), and using the wrong JDK breaks the build silently or loudly.

Useful authoritative sources to fetch directly (prefer these over blog posts):
- `https://meta.fabricmc.net/v2/versions/loader` — loader versions (JSON)
- `https://meta.fabricmc.net/v2/versions/yarn/<MC_VERSION>` — mappings for a specific MC version (JSON)
- `https://meta.fabricmc.net/v2/versions/game` — all MC versions Fabric supports (JSON)
- `https://docs.fabricmc.net/develop/loom/` — Loom plugin behavior/config
- `https://github.com/FabricMC/fabric-loom/releases` — Loom version history + breaking changes
- `https://github.com/FabricMC/fabric/releases` — Fabric API releases per MC version
- `https://fabricmc.net/develop/` — general dev portal, template generator

If any web_fetch to `meta.fabricmc.net` is blocked by network restrictions in the sandbox, fall back to web_search and cite what you find; tell the user if you couldn't verify a number directly.

## Step 2 — Confirm findings before generating files

Briefly state the concrete versions you found (MC version, loader, yarn mappings build, Fabric API version, Loom version, Gradle version, Java version) before writing config. If a number seems inconsistent with others (e.g. a Loom version that predates the target MC version's release), search again rather than proceeding.

## Step 3 — Scaffold the project

Standard Fabric mod layout:

```
mymod/
├── .github/
│   └── workflows/
│       └── build.yml            (CI build — see Step 4, required, not optional)
├── build.gradle
├── settings.gradle
├── gradle.properties
├── gradlew / gradlew.bat / gradle/wrapper/  (still committed — CI needs the wrapper even though we never run it locally)
├── src/main/java/com/example/mymod/
│   └── MyMod.java              (main entrypoint, implements ModInitializer)
├── src/client/java/com/example/mymod/  (only if adding client-only code, e.g. rendering)
│   └── MyModClient.java        (implements ClientModInitializer)
└── src/main/resources/
    ├── fabric.mod.json
    ├── mymod.mixins.json        (only if using mixins)
    └── assets/mymod/...         (lang, textures, models — only as needed)
```

Read `references/build-gradle-template.md` for the annotated `build.gradle` and `gradle.properties` templates — fill in the placeholders with the versions confirmed in Step 1/2, don't invent them.

Read `references/fabric-mod-json.md` for the `fabric.mod.json` template and required fields.

Read `references/common-pitfalls.md` before finishing — it covers the mistakes that most often break freshly generated Fabric mods (wrong plugin IDs, obfuscation-related settings for new versions, mixin refmap issues, split client/main sourcesets, etc).

Read `references/github-actions-workflow.md` now as well (not just at Step 4) so the workflow file is scaffolded alongside the rest of the project in one pass, rather than as an afterthought.

## Step 4 — Verify via GitHub Actions CI, never locally

**This project never runs `./gradlew build` (or any Gradle command) locally, on the sandbox, or
on the user's own machine as the verification method.** All builds happen through a GitHub
Actions workflow. This isn't just a fallback for when local Gradle is unavailable — it's the
required build path for this skill, always.

Every scaffolded (or newly-versioned) mod project must include a working CI workflow at
`.github/workflows/build.yml`. Read `references/github-actions-workflow.md` for the annotated
workflow template — it handles JDK setup (matching the Java version confirmed in Step 1),
Gradle caching, running the build, and uploading the built mod jar as a workflow artifact.

After scaffolding/editing the project:
1. Make sure `.github/workflows/build.yml` exists and is current (create it if missing, update it
   if the Java/Gradle version requirements changed in Step 1/2).
2. Commit and push the changes so the workflow actually runs:

```bash
git add .
git commit -m "Scaffold Fabric mod for <MC_VERSION> with CI build workflow"
git push
```

3. Tell the user the push triggers the build automatically, and point them to the repo's Actions
   tab to watch it and download the built jar from the workflow run's artifacts once it succeeds.
   If `git push` fails (no remote configured, no repo yet, auth issue), report the exact error —
   don't claim the mod was verified if it was never actually pushed/built.
4. If the GitHub CLI (`gh`) is available and authenticated, you can also poll the workflow run's
   status (`gh run list`, `gh run view <run-id> --log`) after pushing, to catch and fix a failed
   build automatically rather than leaving it for the user to discover — iterate (fix, commit,
   push, recheck) until the workflow succeeds or you've exhausted reasonable attempts, then report
   the final state honestly.

Never tell the user their mod "builds fine" based on local inspection alone, and never attempt to
install/invoke a local JDK or Gradle installation in the sandbox to self-verify — the CI workflow
is the single source of truth for whether the project actually compiles.

## When just adding content to an existing mod

If the user says "add an item/block/command" etc. to an already-scaffolded mod rather than "build a new mod," you usually don't need to redo Step 1 — check the existing `gradle.properties` for the versions already in use and match them, unless the user is also asking to upgrade to a new MC version (in which case do the full research step for the new target version). Confirm `.github/workflows/build.yml` already exists in the project; if it's missing (e.g. an older project scaffolded before this workflow was standard), add it now. Still push changes and let CI verify — don't run Gradle locally for a small addition either.

## Reminder: no local Gradle, ever

Don't run `./gradlew`, install a JDK, or otherwise attempt to build/compile the mod inside this sandbox or on the user's local machine as a substitute for CI. If the user asks "does it build?", the honest answer is "the CI workflow will tell us once it's pushed" — check the Actions run, don't guess from local static inspection alone.
