# R4J: Simple Resource Holders

* Api [![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.github.peppshabender.r4j/r4j-api/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.github.peppshabender.r4j/r4j-api)
* Java [![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.github.peppshabender.r4j/r4j-api/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.github.peppshabender.r4j/r4j-api)
* Kotlin [![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.github.peppshabender.r4j/r4j-api/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.github.peppshabender.r4j/r4j-api)

R4J is a utility library that dynamically creates classes for the resources available to a source set. R4J consists of a gradle plugin as well as extension modules to make use of the created classes in java/kotlin. Each resource directory is represented by a nested enum, files by enum values respectively. Every enum implements [`io.github.peppshabender.r4j.Resource`](r4j-api/src/main/java/io/github/peppshabender/r4j/api/Resource.java) in order to be picked up by the extension modules.

## Usage

In order to apply the plugin, add the `gradlePluginPortal()` to your `settings.gradle.kts`

```kotlin
pluginManagement {
  repositories {
    gradlePluginPortal()
  }
}
```

and add the relevant dependencies in the `build.gradle.kts`

```kotlin
plugins {
    id("io.github.peppshabender.r4j") version "0.0.1"
}

repositories {
    mavenCentral()
}
```

## R4J

To make full use of the generated classes, include the necessary dependencies for your project via `io.github.peppshabender.r4j.gradle.utils.r4j`. For a Java project, include something like

```kotlin
import io.github.peppshabender.r4j.gradle.utils.r4j

. . .

dependencies {
    implementation(r4j.api)
    implementation(r4j.java)
}
```

in your `build.gradle.kts`.

### Configuration

R4J can optionally be configured using the `r4jConfig {}` extension, the default configuration would look like

```kotlin 
r4jConfig {
  pkg = "generated.r4j"
  className = "Resources"
  keepExtension = false

  buildJava()
}
```
* `pkg`: Sets the package resource holders will be created in (the source set will always be _main/SPECIFIC_SOURCE_SET_)
* `className`: Adjusts the suffix of the class, the prefix will always be the respective source set name
* `keepExtension`: If true, file extensions are kept for the constant names, they are omitted otherwise
* You can also choose between different builders
  - `buildJava()` can be used to build java enums, `buildKotlin()` for kotlin enums respectively
  - In order to supply your own builder, implement a custom [IResourceFileBuilder](r4j-gradle-plugin/src/main/kotlin/io/github/peppshabender/r4j/gradle/spi/IResourceFileBuilder.kt) and supply it via `buildCustom(builder)`

### The Task

R4J provides the `r4j` task which can be manually executed to re-create resource holders. For `buildJava()` and `buildKotlin()` the respective compile task will depend on `r4j` automatically.
