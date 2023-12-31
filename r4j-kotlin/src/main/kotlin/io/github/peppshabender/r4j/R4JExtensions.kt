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
package io.github.peppshabender.r4j

import io.github.peppshabender.r4j.api.Resource
import java.io.InputStream
import java.net.URI
import java.net.URL
import java.nio.charset.Charset


/**
 * @see R4J.asStream
 */
fun Resource.asStream(): InputStream = R4J.asStream(this)

/**
 * Uses [asStream] in a try-with-resources block.
 *
 * @see asStream
 */
inline fun <T> Resource.useStream(block: (InputStream) -> T) = asStream().use(block)

/**
 * @see R4J.asString
 */
fun Resource.asString(charset: Charset = Charset.defaultCharset()) = asStream().use {
    it.readBytes().toString(charset)
}

/**
 * @see R4J.asUrl
 */
fun Resource.asUrl(): URL = R4J.asUrl(this)

/**
 * @see R4J.asUri
 */
fun Resource.asUri(): URI = asUrl().toURI()