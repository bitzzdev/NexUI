# build.gradle / gradle.properties templates

All version placeholders below (anything in `<ANGLE_BRACKETS>`) must be filled in with
values confirmed via research in SKILL.md Step 1 — never with memorized numbers.

## settings.gradle

```gradle
pluginManagement {
    repositories {
        maven { url = "https://maven.fabricmc.net/" }
        mavenCentral()
        gradlePluginPortal()
    }
}
```

## gradle.properties

```properties
# Gradle
org.gradle.jvmargs=-Xmx2G
org.gradle.parallel=true

# Fabric Properties — confirm ALL of these via web research, do not guess
minecraft_version=<MC_VERSION>
yarn_mappings=<MC_VERSION>+build.<BUILD_NUMBER>
loader_version=<LOADER_VERSION>

# Mod Properties
mod_version=1.0.0
maven_group=com.example
archives_base_name=mymod

# Dependencies
fabric_version=<FABRIC_API_VERSION_FOR_THIS_MC_VERSION>
```

## build.gradle

```gradle
plugins {
    id 'fabric-loom' version '<LOOM_VERSION>'
    // Note: as of mid-2026 Loom has two plugin ids depending on the target MC version's
    // obfuscation status — confirm which one applies (see SKILL.md Step 1, item 5):
    //   id 'net.fabricmc.fabric-loom' version '<LOOM_VERSION>'          // non-obfuscated
    //   id 'net.fabricmc.fabric-loom-remap' version '<LOOM_VERSION>'    // obfuscated
    id 'maven-publish'
}

version = project.mod_version
group = project.maven_group

base {
    archivesName = project.archives_base_name
}

repositories {
    // Add any third-party mod repos here (e.g. Modrinth maven, CurseMaven) if depending on other mods
}

loom {
    // splitEnvironmentSourceSets() // uncomment if using a separate src/client sourceset
}

sourceSets {
    // main {
    //     resources { srcDirs += ["src/main/generated"] } // only if using data generation
    // }
}

dependencies {
    minecraft "com.mojang:minecraft:${project.minecraft_version}"
    mappings "net.fabricmc:yarn:${project.yarn_mappings}:v2"
    modImplementation "net.fabricmc:fabric-loader:${project.loader_version}"
    modImplementation "net.fabricmc.fabric-api:fabric-api:${project.fabric_version}"
}

java {
    // Match this to the Java version confirmed for the target MC version in Step 1
    // (recent MC versions require Java 21; older ones may need 17)
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
    withSourcesJar()
}

processResources {
    inputs.property "version", project.version
    filteringCharset "UTF-8"

    filesMatching("fabric.mod.json") {
        expand "version": project.version
    }
}

tasks.withType(JavaCompile).configureEach {
    it.options.release = 21 // match java block above
}

jar {
    from("LICENSE") {
        rename { "${it}_${project.base.archivesName.get()}" }
    }
}
```

## Notes

- `gradle/wrapper/gradle-wrapper.properties` must specify a Gradle version that meets or
  exceeds the minimum required by the chosen Loom version — check the Loom release notes
  (https://github.com/FabricMC/fabric-loom/releases) for "requires Gradle X.Y" statements.
- If the mod needs client-only code (rendering, screens, keybinds), use
  `loom.splitEnvironmentSourceSets()` and put that code under `src/client/java`, with an
  entrypoint class implementing `ClientModInitializer`, registered in `fabric.mod.json`
  under `entrypoints.client`.
- If depending on other mods not on Maven Central/Fabric's maven, CurseMaven or the
  Modrinth maven are common — add the repository under `repositories {}` and confirm the
  exact coordinate string by searching for the mod's Modrinth/CurseForge page.
