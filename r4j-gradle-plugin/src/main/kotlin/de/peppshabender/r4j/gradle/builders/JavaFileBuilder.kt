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
package de.peppshabender.r4j.gradle.builders

import de.peppshabender.r4j.api.Resource
import de.peppshabender.r4j.gradle.R4JConfig
import de.peppshabender.r4j.gradle.spi.IResourceFileBuilder

/**
 * Builds a simple java enum that implements [Resource].
 */
internal class JavaFileBuilder private constructor(
    className: String,
    private val config: R4JConfig,
    private val indent: Int
): ResourceClassBuilder(
    String.format(HEADER_FORMAT, className, Resource::class.qualifiedName),
    config,
    indent
) {
    override val fileExtension: String = "java"
    override val moduleName: String = "java"
    override val terminators: List<String> = listOf(
        "private final java.lang.String r; private $className(java.lang.String r) { this.r = r; }",
        "@Override public java.lang.String path() { return this.r; }"
    )

    override var nested: List<JavaFileBuilder> = emptyList()

    override fun resourceCode(identifier: String, value: String): String = "$identifier(\"$value\"),"

    override fun addNested(className: String): IResourceFileBuilder =
        JavaFileBuilder(className, this.config, this.indent + 1).also {
            this.nested += it
        }

    companion object {
        private const val HEADER_FORMAT = "public enum %s implements %s {"

        operator fun invoke(className: String, config: R4JConfig): JavaFileBuilder =
            JavaFileBuilder(className, config, 1)
    }
}