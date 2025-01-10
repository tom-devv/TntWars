val minecraftVersion = project.properties["minecraftVersion"]!!.toString();

plugins {
    id("java")
    id("com.gradleup.shadow") version "9.0.0-beta4"
    id("xyz.jpenilla.run-paper") version "2.3.1"

}

repositories {
    mavenCentral()
    maven {
        name = "papermc"
        url = uri("https://repo.papermc.io/repository/maven-public/") // paper
    }
    maven(url = "https://repo.extendedclip.com/content/repositories/placeholderapi/") //papi
    maven("https://jitpack.io") // tab
    maven {
        name = "CodeMC"
        url = uri("https://repo.codemc.io/repository/maven-public/")
    }
}



dependencies {
    implementation("org.spongepowered:configurate-yaml:4.1.2")

    implementation("com.github.javafaker:javafaker:1.0.2") {
        exclude(module = "snakeyaml")
    }
    implementation("org.yaml:snakeyaml:1.26")
    implementation("dev.jorel:commandapi-bukkit-shade-mojang-mapped:9.7.0") // commandapi
    implementation("net.kyori:adventure-api:4.18.0") // adventure (minimessage)
    implementation("net.kyori:adventure-text-minimessage:4.18.0")
    implementation("de.tr7zw:item-nbt-api:2.14.1") //nbt
    compileOnly("com.github.NEZNAMY", "TAB-API", "5.0.3") // tab
    compileOnly ("me.clip:placeholderapi:2.11.6") //papi
    compileOnly("io.papermc.paper:paper-api:${minecraftVersion}-R0.1-SNAPSHOT")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
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

val wslPath =  "/home/kynes/PaperServer/plugins"
val macPath =  "/Users/tompark/Code/Java/PaperServer/plugins"

tasks.shadowJar {
    manifest {
        attributes["paperweight-mappings-namespace"] = "mojang"
    }

    archiveBaseName.set("ProjectLazer")
    archiveClassifier.set("")
    archiveVersion.set(version.toString())


    relocate("dev.jorel.commandapi", "dev.tom.commandapi")
    relocate("de.tr7zw.changeme.nbtapi", "dev.tom.nbtapi")


    destinationDirectory.set(file(wslPath))


}


tasks.processResources {
    filesMatching("plugin.yml") {
        expand(
            "version" to project.version,
            "description" to project.description
        )
    }
}

tasks.runServer{
    minecraftVersion(minecraftVersion)
}