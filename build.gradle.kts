import org.jetbrains.kotlin.gradle.idea.proto.generated.ideaExtrasProto

plugins {
    kotlin("jvm") version embeddedKotlinVersion apply false
    id("com.github.hierynomus.license") version "0.16.1"
}

allprojects {
    group = "de.peppshabender.r4j"
    version = "0.0.1-SNAPSHOT"
}

val licenseFile: File = file("LICENSE")
subprojects {
    apply(plugin = "com.github.hierynomus.license")

    license {
        this.header = licenseFile
    }
}