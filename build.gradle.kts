val minecraftVersion = project.properties["minecraftVersion"]!!.toString();

plugins {
    id("java")
    id("com.gradleup.shadow") version "9.0.0-beta4"
}

repositories {
    mavenCentral()
    maven {
        name = "papermc"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
}

dependencies {
    implementation("org.spongepowered:configurate-yaml:4.1.2")

    implementation("com.github.javafaker:javafaker:1.0.2") {
        exclude(module = "snakeyaml")
    }
    implementation("org.yaml:snakeyaml:1.26")

    compileOnly("io.papermc.paper:paper-api:${minecraftVersion}-R0.1-SNAPSHOT")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(22)
    }
}

tasks.build {
    dependsOn("clean", "shadowJar")
}

tasks.jar {
    manifest {
        attributes["paperweight-mappings-namespace"] = "mojang"
    }
}


tasks.shadowJar {
    manifest {
        attributes["paperweight-mappings-namespace"] = "mojang"
    }

    archiveBaseName.set("ProjectLazer")
    archiveClassifier.set("")
    archiveVersion.set(version.toString())


}


tasks.processResources {
    filesMatching("plugin.yml") {
        expand(
            "version" to project.version,
            "description" to project.description
        )
    }
}

configureBuildTask("buildWindows", "/home/kynes/PaperServer/plugins")
configureBuildTask("buildMac", "/Users/tompark/Code/Java/PaperServer/plugins")

fun configureBuildTask(taskName: String, path: String){
    tasks.register(taskName) {
        group = "build"
        description = "Builds the plugin and copies it to the $taskName server plugins folder"
        doLast {
            val shadowJarTask = tasks.named("shadowJar", com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar::class.java).get()
            val originalDestination = shadowJarTask.destinationDirectory.get()

            println("Current destination directory: $originalDestination")

            shadowJarTask.destinationDirectory.set(file(path))
            println("Relocated destination directory: ${shadowJarTask.destinationDirectory.get()}")

            shadowJarTask.actions.forEach { action ->
                action.execute(shadowJarTask)
            }
        }
    }
}