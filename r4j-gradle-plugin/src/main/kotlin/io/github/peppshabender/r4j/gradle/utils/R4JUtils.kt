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

import io.github.peppshabender.r4j.gradle.R4JConfig
import io.github.peppshabender.r4j.gradle.utils.R4JConstants.r4jDir
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.SourceSetContainer
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import java.nio.file.Path

internal val Project.isKotlinMultiplatform: Boolean
    get() = this.plugins.hasPlugin("org.jetbrains.kotlin.multiplatform")

/**
 * Collects all source sets of the project via the [JavaPluginExtension].
 */
internal val Project.jSourceSets: SourceSetContainer
    get() = this.extensions.getByType(JavaPluginExtension::class.java).sourceSets

/**
 * Collects all source sets of the project via the [KotlinMultiplatformExtension].
 */
internal val Project.kSourceSets: NamedDomainObjectContainer<KotlinSourceSet>
    get() = this.extensions.getByType(KotlinMultiplatformExtension::class.java).sourceSets

/**
 * Fetches the current [R4JConfig] for the project.
 */
internal val Project.r4jConfig
    get() = this.extensions.findByType(R4JConfig::class.java) ?: R4JConfig()

/**
 * Returns the build directory for r4j files.
 */
internal fun Project.r4jModuleDir(moduleName: String, config: R4JConfig): Path = this.r4jDir
    .resolve(moduleName)
    .resolve(config.builder.moduleName)

/**
 * Removes all illegal chars from [this], also prepends an underscore
 * should [this] start with a digit.
 */
internal fun String.normalizeClassName(): String {
    var name = ""
    var us= false

    for(c: Char in this) {
        if(c == '_') {
            us = true
            continue
        } else if(!c.isLetterOrDigit()) {
            continue
        }

        name += if(us) c.uppercase() else c
        us = false
    }

    return (if(name[0].isDigit()) "_" else "") + name
}