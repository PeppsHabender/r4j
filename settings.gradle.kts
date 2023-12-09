rootProject.name = "r4j"

include("r4j-api")
include("r4j-gradle-plugin")

include("r4j-java")
include("r4j-kotlin")
include("r4j-kotlin-compose")

pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}