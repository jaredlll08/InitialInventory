import com.blamejared.initialinventory.gradle.Versions
import com.blamejared.initialinventory.gradle.Properties
plugins {
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
}