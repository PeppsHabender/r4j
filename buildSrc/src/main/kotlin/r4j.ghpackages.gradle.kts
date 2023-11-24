plugins {
    `maven-publish`
}

publishing {
    publications {
        if(components.findByName("java") != null) {
            create<MavenPublication>("mavenJava") {
                from(components["java"])
            }
        }

        if(components.findByName("kotlin") != null) {
            create<MavenPublication>("mavenKotlin") {
                from(components["kotlin"])
            }
        }
    }

    repositories {
        //mavenLocal()
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/peppshabender/r4j")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
}