plugins {
    id("dev.kikugie.stonecutter")
    id("gg.meza.stonecraft")
}

stonecutter active "26.3-neoforge" /* [SC] DO NOT EDIT */

// Regenerates the data for every Minecraft version, from its Fabric build
tasks.register("datagen") {
    group = "minis"
    dependsOn(stonecutter.versions.filter { it.project.endsWith("-fabric") }.map { ":${it.project}:runDatagen" })
}
