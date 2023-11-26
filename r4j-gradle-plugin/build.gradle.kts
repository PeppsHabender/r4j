plugins {
    kotlin("jvm")
    id("com.gradle.plugin-publish") version "1.2.1"
}

repositories {
    mavenCentral()
}

gradlePlugin {
    website = "https://github.com/PeppsHabender/r4j"
    vcsUrl = "https://github.com/PeppsHabender/r4j.git"

    plugins {
        create("r4j-gradle") {
            id = "io.github.peppshabender.r4j"
            displayName = "R4J"
            description = "Plugin for automatically creating resource holders."
            implementationClass = "io.github.peppshabender.r4j.gradle.R4JPlugin"

            tags = listOf("java", "kotlin", "resources", "codegeneration", "code-generation", "code generation")
        }
    }
}

kotlin {
    jvmToolchain(17)
}

tasks.findByName("signPluginMavenPublication")?.enabled = false

dependencies {
    implementation(project(":r4j-api"))
}