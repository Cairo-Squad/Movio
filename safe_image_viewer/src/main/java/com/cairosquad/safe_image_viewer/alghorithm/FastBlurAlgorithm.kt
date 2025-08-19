package com.cairosquad.safe_image_viewer.alghorithm

import android.graphics.Bitmap
import android.graphics.Color
import androidx.compose.runtime.Stable
import androidx.compose.ui.util.lerp
import kotlin.math.exp
import kotlin.math.pow


@Stable
internal fun fastBlurBitmap(input: Bitmap, radius: Int = 5, edgeAlpha: Float = 0.0f): Bitmap {
    val w = input.width
    val h = input.height
    val pixels = IntArray(w * h)
    input.getPixels(pixels, 0, w, 0, 0, w, h)

    val result = pixels.copyOf()
    val sigma = radius / 3.0 // Standard deviation for Gaussian
    val kernelSize = radius * 2 + 1
    val kernel = FloatArray(kernelSize) { i ->
        val x = i - radius
        val weight = exp(-(x.toDouble().pow(2) / (2 * sigma.pow(2))))
        weight.toFloat()
    }
    val kernelSum = kernel.sum()
    for (i in kernel.indices) kernel[i] /= kernelSum

    // Horizontal pass
    for (y in 0 until h) {
        for (x in 0 until w) {
            var rSum = 0f
            var gSum = 0f
            var bSum = 0f
            var aSum = 0f
            var weightSum = 0f

            for (i in -radius..radius) {
                val xIdx = (x + i).coerceIn(0, w - 1)
                val idx = y * w + xIdx
                val weight = kernel[i + radius]
                val c = pixels[idx]
                aSum += Color.alpha(c) * weight
                rSum += Color.red(c) * weight
                gSum += Color.green(c) * weight
                bSum += Color.blue(c) * weight
                weightSum += weight
            }

            val idx = y * w + x
            result[idx] = Color.argb(
                (aSum / weightSum).toInt().coerceIn(0, 255),
                (rSum / weightSum).toInt().coerceIn(0, 255),
                (gSum / weightSum).toInt().coerceIn(0, 255),
                (bSum / weightSum).toInt().coerceIn(0, 255)
            )
        }
    }

    // Vertical pass
    val finalPixels = IntArray(w * h)
    for (x in 0 until w) {
        for (y in 0 until h) {
            var rSum = 0f
            var gSum = 0f
            var bSum = 0f
            var aSum = 0f
            var weightSum = 0f

            for (i in -radius..radius) {
                val yIdx = (y + i).coerceIn(0, h - 1)
                val idx = yIdx * w + x
                val weight = kernel[i + radius]
                val c = result[idx]
                aSum += Color.alpha(c) * weight
                rSum += Color.red(c) * weight
                gSum += Color.green(c) * weight
                bSum += Color.blue(c) * weight
                weightSum += weight
            }

            val idx = y * w + x
            var alpha = (aSum / weightSum).toInt().coerceIn(0, 255)
            val red = (rSum / weightSum).toInt().coerceIn(0, 255)
            val green = (gSum / weightSum).toInt().coerceIn(0, 255)
            val blue = (bSum / weightSum).toInt().coerceIn(0, 255)

            // Apply alpha gradient: fade from 5% to 10% from edges
            val xNorm = x.toFloat() / w
            val yNorm = y.toFloat() / h
            val fadeThreshold = 0.10f
            val distFromEdgeX = minOf(xNorm, 1f - xNorm).coerceIn(0f, 1f)
            val distFromEdgeY = minOf(yNorm, 1f - yNorm).coerceIn(0f, 1f)
            val distFromEdge = minOf(distFromEdgeX, distFromEdgeY)

            val fadeFactor = if (distFromEdge >= fadeThreshold) {
                1f
            } else {
                lerp(edgeAlpha, 1f, distFromEdge / fadeThreshold)
            }
            alpha = (fadeFactor * alpha).toInt().coerceIn(0, 255)

            finalPixels[idx] = Color.argb(alpha, red, green, blue)
        }
    }

    return Bitmap.createBitmap(finalPixels, w, h, input.config)
}