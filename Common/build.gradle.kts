import com.blamejared.modtemplate.Utils
plugins {
    java
    `maven-publish`
    id("com.blamejared.modtemplate")
    id("org.spongepowered.gradle.vanilla") version "0.2.1-SNAPSHOT"
}

val minecraftVersion: String by project
val modName: String by project
val modId: String by project
val crafttweakerVersion: String by project
val crafttweakerAPVersion: String by project
val modVersion: String by project

val baseArchiveName = "${modName}-common-${minecraftVersion}"
version = Utils.updatingVersion(modVersion)

base {
    archivesName.set(baseArchiveName)
}

minecraft {
    version(minecraftVersion)
}

dependencies {
    compileOnly("org.spongepowered:mixin:0.8.5")
    compileOnly("com.blamejared.crafttweaker:CraftTweaker-common-${minecraftVersion}:${crafttweakerVersion}")

    annotationProcessor("com.blamejared.crafttweaker:Crafttweaker_Annotation_Processors-${minecraftVersion}:${crafttweakerAPVersion}")
    annotationProcessor("com.blamejared.crafttweaker:CraftTweaker-common-${minecraftVersion}:${crafttweakerVersion}")
}

tasks.processResources {

    val buildProps = project.properties

    filesMatching("pack.mcmeta") {

        expand(buildProps)
    }
}
publishing {
    publications {
        register("mavenJava", MavenPublication::class) {
            artifactId = baseArchiveName
            from(components["java"])
        }
    }

    repositories {
        maven("file://${System.getenv("local_maven")}")
    }
}