plugins {
    kotlin("jvm") version embeddedKotlinVersion
    id("r4j.ghpackages")
}

repositories {
    mavenCentral()
}

dependencies {
    api(project(":r4j-api"))

    implementation(project(":r4j-java"))
}