plugins {
    kotlin("jvm")
    id("r4j.initialize")
}

kotlin {
    jvmToolchain(17)
}

java {
    withJavadocJar()
    withSourcesJar()
}