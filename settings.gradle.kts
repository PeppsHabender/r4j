include("r4j-api")
include("r4j-gradle-plugin")

include("r4j-java")
include("r4j-kotlin")

pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

rootProject.name = "r4j"