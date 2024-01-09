import com.blamejared.gradle.mod.utils.GMUtils
import com.blamejared.initialinventory.gradle.Properties
import com.blamejared.initialinventory.gradle.Versions
import net.darkhax.curseforgegradle.TaskPublishCurseForge
import net.darkhax.curseforgegradle.Constants as CFG_Constants

plugins {
    id("com.blamejared.initialinventory.default")
    id("com.blamejared.initialinventory.loader")
    id("net.minecraftforge.gradle") version ("[6.0,6.2)")
    id("org.spongepowered.mixin") version ("0.7-SNAPSHOT")
    id("com.modrinth.minotaur")
}

minecraft {
    mappings("official", Versions.MINECRAFT)
    accessTransformer(file("src/main/resources/META-INF/accesstransformer.cfg"))
    runs {
        create("client") {
            taskName("Client")
            workingDirectory(project.file("run"))
            ideaModule("${rootProject.name}.${project.name}.main")
            mods {
                create(Properties.MODID) {
                    source(sourceSets.main.get())
                    source(project(":common").sourceSets.main.get())
                }
            }
        }
    }
}

dependencies {
    "minecraft"("net.minecraftforge:forge:${Versions.MINECRAFT}-${Versions.FORGE}")
    compileOnly(project(":common"))
    annotationProcessor("org.spongepowered:mixin:0.8.5-SNAPSHOT:processor")
    implementation(fg.deobf("com.blamejared.crafttweaker:CraftTweaker-forge-${Versions.MINECRAFT}:${Versions.CRAFTTWEAKER}"))
    annotationProcessor("com.blamejared.crafttweaker:Crafttweaker_Annotation_Processors:${Versions.CRAFTTWEAKER_ANNOTATION_PROCESSOR}")
}

sourceSets.configureEach {
    val dir = layout.buildDirectory.dir("sourcesSets/$this.name")
    this.output.setResourcesDir(dir)
    this.java.destinationDirectory.set(dir)
}

tasks.create<TaskPublishCurseForge>("publishCurseForge") {
    dependsOn(tasks.jar)
    apiToken = GMUtils.locateProperty(project, "curseforgeApiToken") ?: 0

    val mainFile = upload(Properties.CURSE_PROJECT_ID, tasks.jar.get().archiveFile)
    mainFile.changelogType = "markdown"
    mainFile.changelog = GMUtils.smallChangelog(project, Properties.GIT_REPO)
    mainFile.releaseType = CFG_Constants.RELEASE_TYPE_RELEASE
    mainFile.addJavaVersion("Java ${Versions.JAVA}")
    mainFile.addRequirement("crafttweaker")

    doLast {
        project.ext.set("curse_file_url", "${Properties.CURSE_HOMEPAGE}/files/${mainFile.curseFileId}")
    }
}

modrinth {
    token.set(GMUtils.locateProperty(project, "modrinth_token"))
    projectId.set(Properties.MODRINTH_PROJECT_ID)
    changelog.set(GMUtils.smallChangelog(project, Properties.GIT_REPO))
    versionName.set("Forge-${Versions.MINECRAFT}-$version")
    versionType.set("release")
    uploadFile.set(tasks.jar.get())
    dependencies {
        required.project("crafttweaker")
    }
}
tasks.modrinth.get().dependsOn(tasks.jar)