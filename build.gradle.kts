import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm") version "1.6.10"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "com.github.shatteredsoftware"
version = "1.0.0"

repositories {
    maven(url = "https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven(url = "https://repo.extendedclip.com/content/repositories/placeholderapi/")
    mavenCentral()
}

val include = configurations.create("include")
val implementation = configurations.getByName("implementation")
implementation.extendsFrom(include)

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.spigotmc:spigot-api:1.18.2-R0.1-SNAPSHOT")
    implementation("me.clip:placeholderapi:2.11.1")
    include("net.kyori:adventure-api:4.10.1")
    include("net.kyori:adventure-platform-bukkit:4.1.0")
    include("net.kyori:adventure-text-minimessage:4.10.1")
}

tasks.shadowJar {
    configurations = listOf(include)
}