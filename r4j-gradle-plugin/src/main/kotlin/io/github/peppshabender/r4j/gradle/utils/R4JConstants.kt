/**
 * MIT License
 *
 * Copyright (c) 2023 PeppsHabender
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package io.github.peppshabender.r4j.gradle.utils

import org.gradle.api.Project
import java.nio.file.Path

/**
 * What the name says.. Duh
 */
object R4JConstants {
    /**
     * r4j.
     */
    internal const val R4J_STR = "r4j"

    /**
     * r4jConfig. .
     * .
     */
    internal const val R4J_CONFIG_STR = "${R4J_STR}Config"

    /**
     * Build directory for all R4J related stuff.
     */
    internal val Project.r4jDir: Path
        get() = this.buildDir.resolve("generated").resolve("sources").resolve("r4j").toPath()
}

object R4JDeps {
    private const val DEPENDENCY_FORMAT: String = "io.github.peppshabender.r4j:r4j-%s:0.0.1"

    val api: String = DEPENDENCY_FORMAT.format("api")
    val java: String = DEPENDENCY_FORMAT.format("java")
    val kotlin: String = DEPENDENCY_FORMAT.format("kotlin")
}

val Project.r4j: R4JDeps
    get() = R4JDeps