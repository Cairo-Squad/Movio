package com.cairosquad.safe_image_viewer.safe_image_viewer

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.drawscope.DrawScope.Companion.DefaultFilterQuality
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.cairosquad.safe_image_viewer.R
import com.cairosquad.safe_image_viewer.classifier.SafeImageClassifier
import com.cairosquad.safe_image_viewer.loader.CoilImageLoader
import com.cairosquad.safe_image_viewer.modifier.imageBlur
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


/**
 * Displays an image with automatic NSFW classification and optional blur effect.
 *
 * This composable loads an image from the given [model], classifies it using an on-device
 * TensorFlow Lite model, and applies a blur if the content is considered inappropriate.
 *
 * - If the image is classified as NSFW, a blur will be applied (on Android S+ using `Modifier.blur`,
 *   or via a custom bitmap blur for older versions).
 * - Tapping the image toggles blur visibility if [onToggleBlur] is provided.
 *
 * @param model The image source, typically a URL or file path.
 * @param contentDescription Content description for accessibility.
 * @param modifier Modifier applied to the image container.
 * @param contentScale Scaling strategy for the image content.
 * @param alignment Alignment of the image content.
 * @param alpha Opacity level of the image (1f = fully opaque).
 * @param filterQuality Quality level for image rendering.
 * @param colorFilter Optional color filter to apply.
 * @param blur Blur radius in dp (applied only if image is classified as unsafe).
 * @param nudeThreshold Threshold for NSFW classification (lower = more sensitive) and the range is between 0 - 1.
 * @param nonNudeThreshold Threshold for SFW classification (higher = stricter) and the range is between 0 - 1.
 * @param enableLog Whether to log classification results (for debugging).
 * @param placeholder Static painter shown while loading.
 * @param error Static painter shown if image loading fails.
 * @param loadingPlaceholder Composable shown while classification is pending.
 * @param onToggleBlur Optional composable (e.g., icon or button) shown over blurred images.
 *                     Triggered to toggle blur state when tapped.
 *```
 * SafeImageViewer(
 *      modifier = Modifier.size(240.dp),
 *      model = it.url,
 *      contentDescription = "",
 *      loadingPlaceholder = {
 *          Box(modifier = Modifier.background(Color.Gray))
 *      },
 *      contentScale = ContentScale.FillBounds,
 *      blur = 50,
 *      enableLog = true,
 *      placeholder = painterResource(R.drawable.placeholder),
 *      error = painterResource(R.drawable.error),
 *      nudeThreshold = 0.095,
 *      nonNudeThreshold = 0.885,
 *      onToggleBlur = {
 *          BasicText(
 *              "Change Blur",
 *              style = TextStyle(color = Color.White, fontSize = 16.sp)
 *          )
 *      }
 * )
 *```
 *
 */

@Composable
fun SafeImageViewer(
    // required core
    model: String,
    contentDescription: String,
    // appearance and layout
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    alignment: Alignment = Alignment.Center,
    alpha: Float = DefaultAlpha,
    filterQuality: FilterQuality = DefaultFilterQuality,
    colorFilter: ColorFilter? = null,
    // NSFW and blur behavior
    blur: Int = 25,
    nudeThreshold: Double = 0.16,
    nonNudeThreshold: Double = 0.79,
    enableLog: Boolean = false,
    // UI-related
    placeholder: Painter = painterResource(R.drawable.placeholder),
    error: Painter = painterResource(R.drawable.error),
    loadingPlaceholder: @Composable () -> Unit = {},
    onToggleBlur: (@Composable () -> Unit)? = null,
) {
    val context = LocalContext.current

    var isImageSafe by remember { mutableStateOf(true) }
    var hasClassificationCompleted by remember { mutableStateOf(false) }
    var isBlurEnabled by remember { mutableStateOf(true) }
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(model) {
        hasClassificationCompleted = false
        withContext(Dispatchers.IO) {
            val classifier = SafeImageClassifier(context)
            bitmap = CoilImageLoader(context).loadBitmap(model)

            if (bitmap != null && nudeThreshold != 0.0 && nonNudeThreshold != 0.0) {
                withContext(Dispatchers.Unconfined) {
                    isImageSafe = classifier.isInappropriate(
                        bitmap = bitmap!!,
                        nsfwThreshold = nudeThreshold,
                        sfwThreshold = nonNudeThreshold,
                        isLogEnabled = enableLog
                    )
                }
            }
        }
        hasClassificationCompleted = true
    }
    Crossfade(
        modifier = modifier,
        targetState = hasClassificationCompleted
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (it) {
                if (bitmap != null) {
                    AsyncImage(
                        modifier = Modifier
                            .matchParentSize()
                            .imageBlur(
                                isBlurEnabled = isBlurEnabled,
                                isImageSafe = isImageSafe,
                                blur = blur.dp,
                                bitmap = bitmap!!
                            ),
                        model = bitmap,
                        contentDescription = contentDescription,
                        contentScale = contentScale,
                        filterQuality = filterQuality,
                        alpha = alpha,
                        alignment = alignment,
                        colorFilter = colorFilter,
                        placeholder = placeholder,
                        error = error,
                    )
                    if (!isImageSafe && onToggleBlur != null) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .clickable { isBlurEnabled = !isBlurEnabled }
                        ) {
                            onToggleBlur()
                        }
                    }
                }
            } else {
                loadingPlaceholder()
            }
        }
    }
}
