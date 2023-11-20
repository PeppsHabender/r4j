plugins {
    kotlin("jvm") version embeddedKotlinVersion
    `maven-publish`
}

publishing {
    publications {
        create<MavenPublication>("mavenKotlin") {
            from(components["kotlin"])
        }
    }

    repositories {
        mavenLocal()
    }
}

repositories {
    mavenCentral()
}

dependencies {
    api(project(":r4j-api"))

    implementation(project(":r4j-java"))
}