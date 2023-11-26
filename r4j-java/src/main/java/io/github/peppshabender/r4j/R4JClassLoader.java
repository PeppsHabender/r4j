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
import java.nio.charset.StandardCharsets;

/**
 * Base class for loading {@link Resource} objects. Uses classloaders for resource
 * resolving. For default resource loading refer to {@link R4J}.
 *
 * @see R4J
 */
public final class R4JClassLoader {
    private R4JClassLoader() {
        // Utils class
    }

    /**
     * Loads the given resource as an {@link InputStream} from the current threads
     * {@link Thread#getContextClassLoader()}.
     *
     * @param resource Resource to load
     *
     * @return null when the resource is not accessible, the found
     * input stream otherwise
     */
    public static InputStream asStream(final Resource resource) {
        return asStream(resource, contextClassLoader());
    }

    /**
     * Loads the given resource as an {@link InputStream} from the given classloader.
     *
     * @param resource Resource to load
     * @param classLoader Classloader to load resource from
     *
     * @return null when the resource is not accessible, the found
     * input stream otherwise
     */
    public static InputStream asStream(final Resource resource, final ClassLoader classLoader) {
        return classLoader.getResourceAsStream(normalize(resource));
    }

    /**
     * Loads the given resource as a String using {@link Charset#defaultCharset()}.
     * Uses {@link #asStream(Resource, ClassLoader)} to read the string from the current threads
     * {@link Thread#getContextClassLoader()}.
     *
     * @param resource Resource to load
     *
     * @return null when the resource is not accessible, the found
     * string otherwise
     *
     * @see #asStream(Resource, ClassLoader)
     * @see #asString(Resource, Charset)
     * @see #asString(Resource, Charset, ClassLoader)
     */
    public static String asString(final Resource resource) {
        return asString(resource, StandardCharsets.UTF_8);
    }

    /**
     * Loads the given resource as a String using the given charset. Uses
     * {@link #asStream(Resource, ClassLoader)} to read the string from the current threads
     * {@link Thread#getContextClassLoader()}.
     *
     * @param resource Resource to load
     * @param charset Charset to use
     *
     * @return null when the resource is not accessible, the found
     * string otherwise
     *
     * @see #asStream(Resource, ClassLoader)
     * @see #asString(Resource, Charset, ClassLoader)
     */
    public static String asString(final Resource resource, final Charset charset) {
        return asString(resource, charset, contextClassLoader());
    }

    /**
     * Loads the given resource as a String using {@link Charset#defaultCharset()}.
     * Uses {@link #asStream(Resource, ClassLoader)} to read the string from the given classloader.
     *
     * @param resource Resource to load
     * @param classLoader Classloader to load resource from
     *
     * @return null when the resource is not accessible, the found
     * string otherwise
     *
     * @see #asStream(Resource, ClassLoader)
     * @see #asString(Resource, Charset, ClassLoader)
     */
    public static String asString(final Resource resource, final ClassLoader classLoader) {
        return asString(resource, Charset.defaultCharset(), classLoader);
    }

    /**
     * Loads the given resource as a String using the given charset from the given classloader.
     * Uses {@link #asStream(Resource, ClassLoader)} to read the string.
     *
     * @param resource Resource to load
     * @param charset Charset to use
     * @param classLoader Classloader to load resource from
     *
     * @return null when the resource is not accessible, the found
     * string otherwise
     *
     * @see #asStream(Resource, ClassLoader)
     */
    public static String asString(final Resource resource, final Charset charset, final ClassLoader classLoader) {
        try(final InputStream is = asStream(resource, classLoader)) {
            // Should never be null in practice, but we'll just make sure
            return is == null ? null : new String(is.readAllBytes(), charset);
        } catch (final IOException ignored) {
            // Can't do much about that :/ soo null
            return null;
        }
    }

    /**
     * Loads the given resource as an {@link URL} from the current threads
     * {@link Thread#getContextClassLoader()}.
     *
     * @param resource Resource to load
     *
     * @return null when the resource is not accessible, the found
     * url otherwise
     *
     * @see #asUrl(Resource, ClassLoader)
     */
    public static URL asUrl(final Resource resource) {
        return asUrl(resource, contextClassLoader());
    }

    /**
     * Loads the given resource as an {@link URL} from the given classloader.
     *
     * @param resource Resource to load
     * @param classLoader Classloader to load resource from
     *
     * @return null when the resource is not accessible, the found
     * url otherwise
     *
     * @see #asUri(Resource)
     */
    public static URL asUrl(final Resource resource, final ClassLoader classLoader) {
        return classLoader.getResource(normalize(resource));
    }

    /**
     * Loads the given resource as an {@link URL} from the current threads
     * {@link Thread#getContextClassLoader()}. Uses {@link #asUrl(Resource)} internally.
     *
     * @param resource Resource to load
     *
     * @return null when the resource is not accessible, the found
     * uri otherwise
     *
     * @throws URISyntaxException When the uri is invalid
     *
     * @see #asUri(Resource, ClassLoader)
     */
    public static URI asUri(final Resource resource) throws URISyntaxException {
        return asUri(resource, contextClassLoader());
    }

    /**
     * Loads the given resource as an {@link URL} from the current threads
     * {@link Thread#getContextClassLoader()}. Uses {@link #asUrl(Resource)} internally.
     *
     * @param resource Resource to load
     *
     * @return null when the resource is not accessible, the found
     * uri otherwise
     *
     * @throws URISyntaxException When the uri is invalid
     *
     * @see #asUrl(Resource, ClassLoader)
     */
    public static URI asUri(final Resource resource, final ClassLoader classLoader) throws URISyntaxException {
        final URL asUrl = asUrl(resource, classLoader);
        return asUrl == null ? null : asUrl.toURI();
    }

    private static String normalize(final Resource resource) {
        final String path = resource.path();
        return path.startsWith("/") ? path.substring(1) : path;
    }

    private static ClassLoader contextClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }
}
