package com.cairosquad.safe_image_viewer.loader

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.compose.runtime.Immutable
import coil.ImageLoader
import coil.request.ImageRequest

@Immutable
internal class CoilImageLoader(private val context: Context) {
    suspend fun loadBitmap(url: String): Bitmap? {
        try {
            val loader = ImageLoader(context)
            val request = ImageRequest.Builder(context)
                .data(url)
                .allowHardware(false)
                .build()
            val drawableResult = loader.execute(request).drawable
            return (drawableResult as? BitmapDrawable)?.bitmap
        } catch (e: Exception) {
            //handel exception here
            // i return null to use it at ui
            return null
        }
    }
}