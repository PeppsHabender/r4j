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
package de.peppshabender.r4j.gradle.spi;

/**
 * Interface for extending the plugin with a custom file builder.
 */
interface IResourceFileBuilder {
    /**
     * Extension the created file will have.
     */
    val fileExtension: String

    /**
     * Module the resulting file should be in.
     * E.g. "java" for java classes or "kotlin" for kt files.
     */
    val moduleName: String

    /**
     * Adds the given resource to this class.
     */
    fun addResource(r: R.Resource)

    /**
     * Adds a new "nested" class with the given name.
     *
     * @return [IResourceFileBuilder], normally this
     */
    fun addNested(className: String): IResourceFileBuilder

    /**
     * Builds the given class 
     */
    fun build(): String
}
