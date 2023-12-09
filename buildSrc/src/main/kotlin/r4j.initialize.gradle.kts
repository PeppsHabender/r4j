import org.gradle.api.internal.provider.DefaultProvider
import java.net.HttpURLConnection
import java.net.URI

plugins {
    `maven-publish`
    signing
    id("com.github.hierynomus.license")
}


repositories {
    mavenCentral()
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("mavenJava") {
                from(components["java"]!!)
                groupId = this@afterEvaluate.group as String
                artifactId = this@afterEvaluate.name
                version = this@afterEvaluate.version as String

                pom {
                    name.set("R4J")
                    description = this@afterEvaluate.description
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

            addCentralRepo()
        }
    }
}

fun RepositoryHandler.addCentralRepo() {
    val conn: HttpURLConnection = URI.create("https://s01.oss.sonatype.org/service/local/repo_groups/public/content/io/github/peppshabender/r4j/$name/$version/")
        .toURL().openConnection() as HttpURLConnection

    if(conn.responseCode < 400) {
        // Already published -> We cant override
        return
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

signing {
    sign(publishing.publications)
}


license {
    this.header = file("../LICENSE")
}