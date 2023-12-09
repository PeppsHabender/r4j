package io.github.peppshabender.r4j.gradle.utils

import org.gradle.api.Project

/**
 * Contains dependency versions for r4j extensions.
 */
object R4JDeps {
    private const val DEPENDENCY_FORMAT: String = "io.github.peppshabender.r4j:r4j-%s:%s"

    val api: String = DEPENDENCY_FORMAT.format("api", "0.0.1")
    val java: String = DEPENDENCY_FORMAT.format("java", "0.0.1")
    val kotlin: String = DEPENDENCY_FORMAT.format("kotlin", "0.0.2")
    val kotlinCompose: String = DEPENDENCY_FORMAT.format("kotlin-compose", "0.0.1")
}

/**
 * Accesses r4j dependencies via [R4JDeps].
 */
val Project.r4j: R4JDeps
    get() = R4JDeps