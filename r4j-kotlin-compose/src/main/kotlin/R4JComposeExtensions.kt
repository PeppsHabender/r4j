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
import androidx.compose.foundation.Image
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.ResourceLoader
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.loadSvgPainter
import androidx.compose.ui.res.loadXmlImageVector
import io.github.peppshabender.r4j.api.Resource

/**
 * Load a [Painter] from this resource for the application and decode
 * it based on the file extension.
 * Resources for the application are in `src/main/resources` in JVM Gradle projects.
 * Resource loading is not directly related to JVM classloader resources, so resources
 * loading from dependencies JARs may stop working.
 * See [ResourceLoader] for more information.
 *
 * Supported formats:
 * - SVG
 * - XML vector drawable
 *   (see https://developer.android.com/guide/topics/graphics/vector-drawable-resources)
 * - raster formats (BMP, GIF, HEIF, ICO, JPEG, PNG, WBMP, WebP)
 *
 * To load an image from other places (file storage, database, network), use these
 * functions inside [LaunchedEffect] or [remember]:
 * [loadImageBitmap]
 * [loadSvgPainter]
 * [loadXmlImageVector]
 *
 * @return [Painter] used for drawing the loaded resource
 */
@Composable
fun Resource.painterResource(): Painter = androidx.compose.ui.res.painterResource(path())

/**
 * Paint the content using [painterResource].
 *
 * @param sizeToIntrinsics `true` to size the element relative to [Painter.intrinsicSize]
 * @param alignment specifies alignment of the [painterResource] relative to content
 * @param contentScale strategy for scaling [painterResource] if its size does not match the content size
 * @param alpha opacity of [painterResource]
 * @param colorFilter optional [ColorFilter] to apply to [painterResource]
 *
 * @sample androidx.compose.ui.samples.PainterModifierSample
 */
@Composable
fun Modifier.paint(
    resource: Resource,
    sizeToIntrinsics: Boolean = true,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Inside,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null
): Modifier = paint(
    resource.painterResource(),
    sizeToIntrinsics,
    alignment,
    contentScale,
    alpha,
    colorFilter
)

/**
 * A Material Design icon component that draws [painterResource] using [tint], with a default value
 * of [LocalContentColor]. If [painterResource] has no intrinsic size, this component will use the
 * recommended default size. Icon is an opinionated component designed to be used with single-color
 * icons so that they can be tinted correctly for the component they are placed in. For multicolored
 * icons and icons that should not be tinted, use [Color.Unspecified] for [tint]. For generic images
 * that should not be tinted, and do not follow the recommended icon size, use the generic
 * [androidx.compose.foundation.Image] instead. For a clickable icon, see [IconButton].
 *
 * @param contentDescription text used by accessibility services to describe what this icon
 * represents. This should always be provided unless this icon is used for decorative purposes,
 * and does not represent a meaningful action that a user can take. This text should be
 * localized, such as by using [androidx.compose.ui.res.stringResource] or similar
 * @param modifier optional [Modifier] for this Icon
 * @param tint tint to be applied to [androidx.compose.ui.res.painterResource]. If [Color.Unspecified] is provided,
 * then no tint is applied
 */
@Composable
fun Resource.Icon(
    contentDescription: String? = null,
    modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current.copy(alpha = LocalContentAlpha.current)
) {
    androidx.compose.material.Icon(
        painterResource(),
        contentDescription,
        modifier,
        tint
    )
}

/**
 * Creates a composable that lays out and draws [painterResource]. This will attempt to size
 * the composable according to the [Painter]'s intrinsic size. However, an optional [Modifier]
 * parameter can be provided to adjust sizing or draw additional content (ex. background)
 *
 * **NOTE** a Painter might not have an intrinsic size, so if no LayoutModifier is provided
 * as part of the Modifier chain this might size the [Image] composable to a width and height
 * of zero and will not draw any content. This can happen for Painter implementations that
 * always attempt to fill the bounds like [ColorPainter]
 *
 * @sample androidx.compose.foundation.samples.BitmapPainterSample
 *
 * @param contentDescription text used by accessibility services to describe what this image
 * represents. This should always be provided unless this image is used for decorative purposes,
 * and does not represent a meaningful action that a user can take. This text should be
 * localized, such as by using [androidx.compose.ui.res.stringResource] or similar
 * @param modifier Modifier used to adjust the layout algorithm or draw decoration content (ex.
 * background)
 * @param alignment Optional alignment parameter used to place the [Painter] in the given
 * bounds defined by the width and height.
 * @param contentScale Optional scale parameter used to determine the aspect ratio scaling to be used
 * if the bounds are a different size from the intrinsic size of the [Painter]
 * @param alpha Optional opacity to be applied to the [Painter] when it is rendered onscreen
 * the default renders the [Painter] completely opaque
 * @param colorFilter Optional colorFilter to apply for the [Painter] when it is rendered onscreen
 */
@Composable
fun Resource.Image(
    contentDescription: String? = null,
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null
) {
    androidx.compose.foundation.Image(
        painterResource(),
        contentDescription,
        modifier,
        alignment,
        contentScale,
        alpha,
        colorFilter
    )
}