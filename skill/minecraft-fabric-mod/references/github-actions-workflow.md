# GitHub Actions CI build workflow

This project builds exclusively through GitHub Actions — never locally, never in the sandbox.
Every scaffolded mod must include this workflow at `.github/workflows/build.yml`.

## Base workflow

```yaml
name: Build

on:
  push:
    branches: [ "**" ]
  pull_request:
    branches: [ "**" ]
  workflow_dispatch: {}   # allows manually triggering a build from the Actions tab

permissions:
  contents: read

jobs:
  build:
    runs-on: ubuntu-latest

    steps:
      - name: Checkout
        uses: actions/checkout@v4

      - name: Set up JDK
        uses: actions/setup-java@v4
        with:
          # Use the Java version confirmed in SKILL.md Step 1 for the target Minecraft version
          # (recent MC versions require Java 21) — keep this in sync with build.gradle's
          # sourceCompatibility/targetCompatibility.
          java-version: '21'
          distribution: 'temurin'

      - name: Setup Gradle
        uses: gradle/actions/setup-gradle@v4
        # This action handles Gradle dependency + wrapper caching automatically — no need to
        # hand-roll a cache step separately.

      - name: Make gradlew executable
        run: chmod +x ./gradlew

      - name: Build mod
        run: ./gradlew build --no-daemon

      - name: Upload built jar
        uses: actions/upload-artifact@v4
        with:
          name: mod-jar
          path: build/libs/*.jar
          if-no-files-found: error
```

## Notes on this template

- **`gradle/actions/setup-gradle@v4`** is the current official Gradle GitHub Action for caching
  and running Gradle in CI — prefer it over hand-writing a cache step with `actions/cache`, since
  it correctly handles the Gradle wrapper, dependency cache, and build cache together. Confirm
  the latest tagged version if unsure (search `gradle build action github actions` if the
  referenced version seems outdated).
- **`actions/checkout@v4`, `actions/setup-java@v4`, `actions/upload-artifact@v4`** — check current
  major versions via the Actions marketplace or a quick search if a workflow run fails with a
  deprecation notice; GitHub periodically deprecates old major versions of its own actions.
- **`java-version`** must match whatever Java version was confirmed for the target Minecraft
  version in SKILL.md Step 1 (research phase) — don't leave this as a stale default across MC
  version upgrades; update it alongside `build.gradle`'s Java compatibility settings.
- **`build/libs/*.jar`** is Loom's standard output location for the built (and remapped) mod jar;
  confirm this path still matches if a specific Loom version changes output conventions —
  otherwise the artifact upload step will silently find nothing (mitigated here by
  `if-no-files-found: error`, which fails the workflow loudly instead of succeeding with no jar).
- **Both `push` and `pull_request` triggers** are included so every change gets built automatically,
  whether pushed directly or opened as a PR. `workflow_dispatch` lets you or the user manually
  re-trigger a build from the GitHub UI without needing a new commit.

## Optional: release-on-tag workflow

If the user wants tagged releases to auto-publish the built jar as a GitHub Release (useful once
the mod is stable enough to distribute), add a second workflow or extend this one with a
tag-triggered job — search current GitHub Actions docs for `actions/create-release` or the
current recommended release-action equivalent (this space changes; don't assume an old
action name is still maintained) before wiring this up, and only add it if asked, since not every
project needs auto-publishing.

## Checking the build result

After pushing, don't declare the mod "done" until the workflow has actually been checked:
- If the GitHub CLI (`gh`) is available and authenticated in the environment, use
  `gh run list --branch <branch> --limit 1` and `gh run view <run-id> --log` (or
  `gh run watch <run-id>`) to check status and pull failure logs directly, so a broken build can
  be diagnosed and fixed without waiting on the user to check manually.
- If `gh` isn't available, tell the user to check the repository's **Actions** tab, and that the
  built jar (once the workflow succeeds) is downloadable from that run's **Artifacts** section.
- If the workflow fails, read the actual failure log rather than guessing at the cause — Gradle/
  Fabric Loom build failures are usually specific about which dependency or mapping failed to
  resolve, which points directly back to a Step 1 research step that may need re-checking (e.g. a
  mismatched Yarn mappings build or Fabric API version for the target MC version).
