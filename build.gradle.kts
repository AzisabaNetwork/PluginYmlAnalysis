plugins {
    kotlin("jvm") version "1.6.20"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "net.azisaba"
version = "1.0.1"

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.yaml:snakeyaml:1.30")
    compileOnly("org.jetbrains:annotations:23.0.0")
    compileOnly("org.spigotmc:spigot-api:1.16.5-R0.1-SNAPSHOT")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(11))
}

tasks {
    shadowJar {
        manifest {
            attributes(
                "Main-Class" to "net.azisaba.pluginymlanalysis.Main",
            )
        }
        archiveFileName.set("PluginYmlAnalysis.jar")
    }
}
