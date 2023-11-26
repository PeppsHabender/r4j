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
package io.github.peppshabender.r4j;

import io.github.peppshabender.r4j.api.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * Base class for loading {@link Resource} objects. Uses {@link R4J#getClass()} for resource resolving.
 * For resource loading via class loader, refer to {@link R4JClassLoader}.
 *
 * @see R4JClassLoader
 */
public final class R4J {
    private R4J() {
        // Utils class
    }

    /**
     * Loads the given resource as an {@link InputStream}.
     *
     * @param resource Resource to load
     *
     * @return null when the resource is not accessible, the found
     * input stream otherwise
     */
    public static InputStream asStream(final Resource resource) {
        return R4J.class.getResourceAsStream(resource.path());
    }

    /**
     * Loads the given resource as a String using {@link Charset#defaultCharset()}.
     * Uses {@link #asStream(Resource)} to read the string.
     *
     * @param resource Resource to load
     *
     * @return null when the resource is not accessible, the found
     * string otherwise
     *
     * @see #asStream(Resource)
     * @see #asString(Resource, Charset)
     */
    public static String asString(final Resource resource) {
        return asString(resource, Charset.defaultCharset());
    }

    /**
     * Loads the given resource as a String using the given charset.
     * Uses {@link #asStream(Resource)} to read the string.
     *
     * @param resource Resource to load
     * @param charset Charset to use
     *
     * @return null when the resource is not accessible, the found
     * string otherwise
     *
     * @see #asStream(Resource)
     */
    public static String asString(final Resource resource, final Charset charset) {
        try(final InputStream is = asStream(resource)) {
            // Should never be null in practice, but we'll just make sure
            return is == null ? null : new String(is.readAllBytes(), charset);
        } catch (final IOException ignored) {
            // Can't do much about that :/ soo null
            return null;
        }
    }

    /**
     * Loads the given resource as an {@link URL}.
     *
     * @param resource Resource to load
     * @return null when the resource is not accessible, the found
     * url otherwise
     *
     * @see #asUri(Resource)
     */
    public static URL asUrl(final Resource resource) {
        return R4J.class.getResource(resource.path());
    }

    /**
     * Loads the given resource as an {@link URL}.
     * Uses {@link #asUrl(Resource)} internally.
     *
     * @param resource Resource to load
     * @return null when the resource is not accessible, the found
     * url otherwise
     *
     * @see #asUrl(Resource)
     */
    public static URI asUri(final Resource resource) throws URISyntaxException {
        final URL asUrl = asUrl(resource);
        return asUrl == null ? null : asUrl.toURI();
    }
}
