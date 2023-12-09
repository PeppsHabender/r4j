version = "0.0.2"
description = "R4J Kotlin Adapter"

plugins {
    id("r4j.kotlin-init")
}

dependencies {
    api(project(":r4j-api"))
    implementation(project(":r4j-java"))
}