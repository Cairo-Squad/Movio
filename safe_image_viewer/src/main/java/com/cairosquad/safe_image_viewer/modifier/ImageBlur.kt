package com.cairosquad.safe_image_viewer.modifier

import android.graphics.Bitmap
import android.os.Build
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.unit.Dp

@Stable
fun Modifier.imageBlur(
	blur: Dp,
	bitmap: Bitmap,
	isBlurEnabled: Boolean = true,
	isBlurForced: Boolean = false,
	isImageSafe: Boolean = false
): Modifier {
	return if (isBlurForced) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
			this.blur(blur)
		} else {
			this.fastBlur(bitmap, radius = blur.value.toInt())
		}
	} else if (! isImageSafe && isBlurEnabled) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
			this.blur(blur)
		} else {
			this.fastBlur(bitmap, radius = blur.value.toInt())
		}
	} else {
		this
	}
}