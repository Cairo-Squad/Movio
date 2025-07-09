package com.cairosquad.safe_image_viewer.loader

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.compose.runtime.Immutable
import coil.ImageLoader
import coil.request.ImageRequest

@Immutable
internal class CoilImageLoader(private val context: Context) {
    suspend fun loadBitmap(url: String): Bitmap {
        val loader = ImageLoader(context)
        val request = ImageRequest.Builder(context)
            .data(url)
            .allowHardware(false)
            .build()
        return (loader.execute(request).drawable as BitmapDrawable).bitmap
    }
}