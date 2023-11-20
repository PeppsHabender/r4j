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
package de.peppshabender.r4j.gradle

import de.peppshabender.r4j.gradle.builders.JavaFileBuilder
import de.peppshabender.r4j.gradle.builders.KotlinFileBuilder
import de.peppshabender.r4j.gradle.spi.IResourceFileBuilder
import javax.lang.model.SourceVersion

/**
 * Used to configure the r4j task.
 */
open class R4JConfig {
    /**
     * Package the generated class(es) will reside in.
     *
     * Has to conform to java naming conventions!
     */
    var pkg: String = "generated.r4j"
        set(value) = if (pkgRgx.matches(value)) {
            field = value
        } else throw IllegalArgumentException("$value is not a valid package identifier!")

    /**
     * Suffix for the outer class.
     */
    var className: String = "Resources"
        set(value) {
            if(!SourceVersion.isIdentifier(value) || SourceVersion.isKeyword(value)) {
                throw IllegalArgumentException("$value is not a valid class identifier!")
            }

            field = value
        }

    /**
     * If true, file extensions are kept for the constant names, they are
     * omitted otherwise.
     */
    var keepExtension: Boolean = false
    internal var builder: IFileBuilderProvider = JAVA_BUILDER
    internal var excludedSourceSets: Set<String> = emptySet()

    /**
     * Excludes source sets with the given name from generating.
     *
     * @param sourceSet Source set to exclude
     */
    fun exclude(sourceSet: String) {
        this.excludedSourceSets += sourceSet
    }

    /**
     * Marks r4j to build a java class.
     */
    fun buildJava() {
        this.builder = JAVA_BUILDER
    }

    /**
     * Marks r4j to build a kotlin class.
     */
    fun buildKotlin() {
        this.builder = KOTLIN_BUILDER
    }

    /**
     * Uses the provider to fetch a custom file builder.
     *
     * @param builder Provider for a custom [IResourceFileBuilder]
     */
    fun buildCustom(builder: IFileBuilderProvider) {
        this.builder = builder
    }

    companion object {
        private val pkgRgx: Regex = "[a-z][a-z0-9_]*(\\.[a-z0-9_]+)+[0-9a-z_]".toRegex()

        internal val JAVA_BUILDER: IFileBuilderProvider = object : IFileBuilderProvider {
            override val moduleName: String = "java"

            override fun provide(className: String, config: R4JConfig): IResourceFileBuilder = JavaFileBuilder(className, config)

        }
        internal val KOTLIN_BUILDER: IFileBuilderProvider = object : IFileBuilderProvider {
            override val moduleName: String = "kotlin"

            override fun provide(className: String, config: R4JConfig): IResourceFileBuilder = KotlinFileBuilder(className, config)

        }
    }
}

/**
 * Provides an implementation of [IResourceFileBuilder]. Can be used to
 * generate custom classes.
 */
interface IFileBuilderProvider {
    /**
     * Module this file will be stored in.
     */
    val moduleName: String

    /**
     * Provides a new file builder which can build a class [className]
     * for the provided [config].
     */
    fun provide(className: String, config: R4JConfig): IResourceFileBuilder
}