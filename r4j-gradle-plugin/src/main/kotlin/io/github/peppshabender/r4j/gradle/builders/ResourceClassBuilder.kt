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
package io.github.peppshabender.r4j.gradle.builders

import io.github.peppshabender.r4j.gradle.R4JConfig
import io.github.peppshabender.r4j.gradle.spi.IResourceFileBuilder
import io.github.peppshabender.r4j.gradle.spi.R
import java.nio.file.Path
import kotlin.io.path.absolutePathString
import kotlin.io.path.extension
import kotlin.io.path.name
import kotlin.io.path.nameWithoutExtension

internal abstract class ResourceClassBuilder(
    header: String,
    private val config: R4JConfig,
    private val indent: Int
): IResourceFileBuilder {
    protected abstract val terminators: List<String>
    protected abstract val nested: List<IResourceFileBuilder>

    private var clazz: String = "$header\n".indentLess()
    private var identifiers: Set<String> = emptySet()

    override fun build(): String {
        val builtClass: StringBuilder = StringBuilder()

        if(this.indent == 1) {
            builtClass.append("package ${this.config.pkg};").newLine().newLine();
        }

        builtClass.append(this.clazz).append(';').newLine()
        this.terminators.forEach {
            builtClass.append(it.indent()).newLine()
        }
        this.nested.map(IResourceFileBuilder::build).forEach {
            builtClass.newLine().append(it)
        }

        return builtClass.newLine().append("}".indentLess()).toString()
    }

    final override fun addResource(r: R.Resource) {
        var varName: String = r.varName(this.config)
        if(varName in this.identifiers) {
            // !!! DUPLICATE ALERT !!!
            // Looks like you're gonna get the extension anyway
            varName += "_${r.file.extension.uppercase()}"
        }

        this.identifiers += varName

        this.clazz += "\n"
        this.clazz += resourceCode(varName, r.file.resourcePath()).indent()
    }

    abstract fun resourceCode(identifier: String, value: String): String

    private fun Path.resourcePath(): String =
        absolutePathString().replace('\\', '/').substringAfter("resources")

    private fun R.Resource.varName(config: R4JConfig): String =
        (if(config.keepExtension) this.file.name else this.file.nameWithoutExtension)
            .replace("\\W".toRegex(), "_")
            .replace("_+".toRegex(), "_").let {
                if(it[0].isDigit()) "_$it" else it
            }.uppercase()

    private fun StringBuilder.newLine(): StringBuilder = append(System.lineSeparator())
    private fun String.indent() = "\t".repeat(this@ResourceClassBuilder.indent) + this
    private fun String.indentLess() = "\t".repeat(this@ResourceClassBuilder.indent - 1) + this
}