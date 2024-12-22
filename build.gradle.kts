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

    destinationDirectory.set(file("/home/kynes/PaperServer/plugins"))
}

tasks.processResources {
    filesMatching("plugin.yml") {
        expand(
            "version" to project.version,
            "description" to project.description
        )
    }
}
