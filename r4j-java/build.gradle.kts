plugins {
    id("java")
    `maven-publish`
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }

    repositories {
        mavenLocal()
    }
}

dependencies {
    implementation(project(":r4j-api"))
}