import com.blamejared.gradle.mod.utils.GMUtils
import com.blamejared.initialinventory.gradle.Properties
import com.blamejared.initialinventory.gradle.Versions
import net.darkhax.curseforgegradle.TaskPublishCurseForge
import net.darkhax.curseforgegradle.Constants as CFG_Constants

plugins {
    id("fabric-loom") version "1.4-SNAPSHOT"
    id("com.blamejared.initialinventory.default")
    id("com.blamejared.initialinventory.loader")
    id("com.modrinth.minotaur")
}

dependencies {
    minecraft("com.mojang:minecraft:${Versions.MINECRAFT}")
    mappings(loom.officialMojangMappings())
    modImplementation("net.fabricmc:fabric-loader:${Versions.FABRIC_LOADER}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${Versions.FABRIC}")
    implementation(project(":common"))
    modImplementation("com.blamejared.crafttweaker:CraftTweaker-fabric-${Versions.MINECRAFT}:${Versions.CRAFTTWEAKER}")
    modImplementation("com.faux.fauxcustomentitydata:FauxCustomEntityData-fabric-${Versions.MINECRAFT}:${Versions.FAUX_CUSTOM_ENTITY_DATA}")
    annotationProcessor("com.blamejared.crafttweaker:Crafttweaker_Annotation_Processors:${Versions.CRAFTTWEAKER_ANNOTATION_PROCESSOR}")
}

loom {
    mixin {
        defaultRefmapName.set("${Properties.MODID}.refmap.json")
    }
    runs {
        named("client") {
            client()
            configName = "Fabric Client"
            ideConfigGenerated(true)
            runDir("run")
        }
    }
}

tasks.create<TaskPublishCurseForge>("publishCurseForge") {
    dependsOn(tasks.remapJar)
    apiToken = GMUtils.locateProperty(project, "curseforgeApiToken")

    val mainFile = upload(Properties.CURSE_PROJECT_ID, tasks.remapJar.get().archiveFile)
    mainFile.changelogType = "markdown"
    mainFile.changelog = GMUtils.smallChangelog(project, Properties.GIT_REPO)
    mainFile.releaseType = CFG_Constants.RELEASE_TYPE_RELEASE
    mainFile.addJavaVersion("Java ${Versions.JAVA}")
    mainFile.addGameVersion(Versions.MINECRAFT)
    mainFile.addRequirement("fabric-api")
    mainFile.addRequirement("crafttweaker")
    mainFile.addRequirement("faux-custom-entity-data")

    doLast {
        project.ext.set("curse_file_url", "${Properties.CURSE_HOMEPAGE}/files/${mainFile.curseFileId}")
    }
}

modrinth {
    token.set(GMUtils.locateProperty(project, "modrinth_token"))
    projectId.set(Properties.MODRINTH_PROJECT_ID)
    changelog.set(GMUtils.smallChangelog(project, Properties.GIT_REPO))
    versionName.set("Fabric-${Versions.MINECRAFT}-$version")
    versionType.set("release")
    uploadFile.set(tasks.remapJar.get())
    dependencies {
        required.project("fabric-api")
        required.project("crafttweaker")
        required.project("faux-custom-entity-data")
    }
}
tasks.modrinth.get().dependsOn(tasks.remapJar)