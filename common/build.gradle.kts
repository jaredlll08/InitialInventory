import com.blamejared.initialinventory.gradle.Versions

plugins {
    id("java")
    id("org.spongepowered.gradle.vanilla") version "0.2.1-SNAPSHOT"
    id("com.blamejared.initialinventory.default")
}

minecraft {
    version(Versions.MINECRAFT)
    runs {
        client("Common Client") {
            workingDirectory(project.file("run"))
        }
    }
}

dependencies {
    compileOnly("org.spongepowered:mixin:0.8.5")
    implementation("com.blamejared.crafttweaker:CraftTweaker-common-${Versions.MINECRAFT}:${Versions.CRAFTTWEAKER}")
    annotationProcessor("com.blamejared.crafttweaker:Crafttweaker_Annotation_Processors:${Versions.CRAFTTWEAKER_ANNOTATION_PROCESSOR}")
}