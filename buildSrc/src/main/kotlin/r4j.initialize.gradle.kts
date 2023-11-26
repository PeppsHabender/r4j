import org.gradle.api.internal.provider.DefaultProvider

plugins {
    `maven-publish`
    signing
    id("com.github.hierynomus.license")
}


repositories {
    mavenCentral()
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"]!!)
            groupId = rootProject.group as String
            artifactId = project.name
            version = rootProject.version as String

            pom {
                name.set("R4J")
                description.set(DefaultProvider(project::getDescription))
                url.set("https://github.com/PeppsHabender/r4j")
                packaging = "jar"

                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://opensource.org/license/mit/")
                    }
                }

                scm {
                    url.set("scm:git@github.com/PeppsHabender/r4j.git")
                    connection.set("scm:git@github.com/PeppsHabender/r4j.git")
                    developerConnection.set("scm:git@github.com/PeppsHabender/r4j.git")
                }

                developers {
                    developer {
                        id.set("PeppsHabender")
                    }
                }
            }
        }
    }

    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/peppshabender/r4j")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }

        maven {
            name = "MavenCentral"
            url = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            credentials {
                username = findProperty("ossrhUser")!! as String
                password = findProperty("ossrhPw")!! as String
            }
        }
    }
}

signing {
    sign(publishing.publications)
}


license {
    this.header = file("../LICENSE")
}