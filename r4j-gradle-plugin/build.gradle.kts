import java.nio.file.Path

plugins {
    `java-gradle-plugin`
    kotlin("jvm") version embeddedKotlinVersion
    id("com.gradle.plugin-publish") version "1.2.1"
}

gradlePlugin {
    plugins {
        create("r4j-gradle") {
            id = "de.peppshabender.r4j"
            implementationClass = "de.peppshabender.r4j.gradle.R4JPlugin"
        }
    }
}

tasks.register<DefaultTask>("writeVersion") {
    val propFile: File = file("$buildDir/generated/version.properties")
    this.outputs.file(propFile)

    doLast {
        propFile.parentFile.mkdirs()
        propFile.createNewFile()
        propFile.writeText("version=${project.version}")
    }
}.also {
    tasks.withType<ProcessResources> {
        from(files(it))
    }
}

dependencies {
    implementation(project(":r4j-api"))
}

repositories {
    mavenCentral()
}