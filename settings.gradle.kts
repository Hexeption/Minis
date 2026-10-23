pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.kikugie.dev/releases")
        maven("https://maven.kikugie.dev/snapshots")
        maven("https://maven.fabricmc.net/")
        maven("https://maven.architectury.dev")
        maven("https://maven.minecraftforge.net")
        maven("https://maven.neoforged.net/releases/")
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
    id("gg.meza.stonecraft") version "1.14.5"
    id("dev.kikugie.stonecutter") version "0.9.8"
}

stonecutter {
    centralScript = "build.gradle.kts"
    kotlinController = true
    shared {
        fun mc(version: String, vararg loaders: String) {
            for (it in loaders) version("$version-$it", version)
        }

        // NeoForge has no 1.20.1 release
        mc("1.20.1", "forge", "fabric")
        for (version in listOf("1.20.4", "1.20.6", "1.21.1", "1.21.4", "1.21.8", "1.21.11", "26.1.2", "26.2", "26.3")) {
            mc(version, "neoforge", "forge", "fabric")
        }

        vcsVersion = "26.3-neoforge"
    }
    create(rootProject)
}

rootProject.name = "Minis"
