package com.cairosquad.safe_image_viewer.safe_image_viewer

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import com.cairosquad.safe_image_viewer.classifier.NSFWDetector
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
	enableLog: Boolean = true,
	// UI-related
	placeholder: Painter = painterResource(R.drawable.placeholder),
	error: Painter = painterResource(R.drawable.error),
	onIsImageSafeChanged: (Boolean) -> Unit = {},
	loadingPlaceholder: @Composable () -> Unit = {},
	onToggleBlur: (@Composable () -> Unit)? = null,
) {
	val context = LocalContext.current

	var isImageSafe by remember { mutableStateOf(true) }
	var hasClassificationCompleted by remember { mutableStateOf(false) }
	var isBlurEnabled by remember { mutableStateOf(true) }
	var bitmap by remember { mutableStateOf<Bitmap?>(null) }

	// Track if this composable is still active
	var isActive by remember { mutableStateOf(true) }

	// Reset state when model changes
	LaunchedEffect(model) {
		isActive = true
		hasClassificationCompleted = false
		bitmap = null
		isImageSafe = true
		isBlurEnabled = true
	}

	// Cancel operations when composable is disposed
	DisposableEffect(model) {
		onDispose {
			isActive = false
		}
	}

	LaunchedEffect(isImageSafe) {
		if (isActive) {
			onIsImageSafeChanged(isImageSafe)
		}
	}

	LaunchedEffect(model) {
		if (!isActive) return@LaunchedEffect

		hasClassificationCompleted = false

		try {
			// Load bitmap with cancellation check
			withContext(Dispatchers.Unconfined) {
				if (!isActive) return@withContext
				bitmap = CoilImageLoader(context).loadBitmap(model)
			}

			// Early exit if composable is no longer active
			if (!isActive || bitmap == null) {
				hasClassificationCompleted = true
				return@LaunchedEffect
			}

			// Skip NSFW detection if thresholds are disabled
			if (nudeThreshold == 0.0 && nonNudeThreshold == 0.0) {
				hasClassificationCompleted = true
				return@LaunchedEffect
			}

			// Perform NSFW detection with cancellation support
			withContext(Dispatchers.Default) {
				if (!isActive) return@withContext

				NSFWDetector.isNSFWCancellable(
					bitmap = bitmap!!,
					enableLog = enableLog,
					nudeThreshold = nudeThreshold,
					nonNudeThreshold = nonNudeThreshold,
					isActive = { isActive },
					callback = { isNSFW ->
						if (isActive) {
							isImageSafe = !isNSFW
							hasClassificationCompleted = true
						}
					}
				)
			}
		} catch (e: Exception) {
			if (isActive) {
				if (enableLog) {
					Log.e("SafeImageViewer", "Error processing image: ${e.localizedMessage}")
				}
				hasClassificationCompleted = true
			}
		}
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
								bitmap = bitmap !!
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
					if (! isImageSafe && onToggleBlur != null) {
						Box(
							modifier = Modifier
								.align(Alignment.Center)
								.clickable { isBlurEnabled = ! isBlurEnabled }
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
