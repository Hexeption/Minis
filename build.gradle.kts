import gg.meza.stonecraft.mod

plugins {
    id("gg.meza.stonecraft")
}

val minecraft = stonecutter.current.version
val unobfuscated = stonecutter.eval(minecraft, ">=26.1")

repositories {
    mavenCentral()
    maven("https://pkgs.dev.azure.com/djtheredstoner/DevAuth/_packaging/public/maven/v1") { name = "DevAuth" }
    maven("https://maven.terraformersmc.com/releases/") { name = "TerraformersMC" }
}

dependencies {
    val runtime = if (unobfuscated) "localRuntime" else "modLocalRuntime"

    add(runtime, "me.djtheredstoner:DevAuth-${if (mod.isForge) "forge-latest" else mod.loader}:${property("devauth_version")}")

    if (mod.isFabric) {
        add(runtime, "com.terraformersmc:modmenu:${mod.prop("modmenu_version")}") {
            isTransitive = false
        }
    }
}

loom {
    runs.named("client") {
        jvmArguments.add("-Ddevauth.enabled=true")
    }
    if (mod.isForge) {
        mods {
            register(mod.id) {
                sourceSet(sourceSets.main.get())
            }
        }
    }
}

// fml.startup.GameTestServer only exists from NeoForge 1.21.9
if (mod.isNeoforge && stonecutter.eval(minecraft, ">=1.21.5 <1.21.9")) {
    afterEvaluate {
        loom.runs.named("gameTestServer") {
            mainClass.set("cpw.mods.bootstraplauncher.BootstrapLauncher")
        }
    }
}

// DevAuth supplies the account name

// Forge 1.21.11 can't start a dev server under Architectury Loom
if (mod.isForge && minecraft == "1.21.11") {
    tasks.matching { it.name == "runGameTestServer" }.configureEach { enabled = false }
}
afterEvaluate {
    loom.runs.named("client") {
        programArguments.set(programArguments.get().filterNot { it.startsWith("--username") })
    }
}

stonecutter {
    // Minecraft renamed ResourceLocation to Identifier in 1.21.11
    replacements.string(stonecutter.current.parsed < "1.21.11") {
        replace("Identifier", "ResourceLocation")
    }
}

modSettings {
    runDirectory = rootProject.layout.projectDirectory.dir("run/${project.name}")
    variableReplacements = mapOf(
        "authors" to project.property("mod.authors").toString(),
        "license" to project.property("mod.license").toString(),
        "neoforgeVersionRange" to "[${mod.prop("neoforge_version", "0.0").substringBeforeLast('.')},)",
        "forgeLoaderVersion" to mod.prop("forge_loader_version", ""),
    )
}

publishMods {
    modrinth {
        if (mod.isFabric) requires("fabric-api")
    }
    curseforge {
        client = true
        server = true
        if (mod.isFabric) requires("fabric-api")
    }
}
