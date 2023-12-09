version = "0.0.1"
description = "R4J Jetpack Compose Extensions"

plugins {
    id("r4j.kotlin-init")
    id("org.jetbrains.compose") version "1.5.11"
}

dependencies {
    api(project(":r4j-api"))

    compileOnly("org.jetbrains.compose.desktop:desktop-jvm:1.5.11")
}