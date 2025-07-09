package com.cairosquad.safe_image_viewer.modifier

import android.graphics.Bitmap
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.unit.IntSize
import com.cairosquad.safe_image_viewer.alghorithm.fastBlurBitmap

@Stable
fun Modifier.fastBlur(
    sourceBitmap: Bitmap,
    radius: Int = 6
): Modifier = composed {
    var blurredBitmap by remember { mutableStateOf<ImageBitmap?>(null) }

    LaunchedEffect(sourceBitmap, radius) {
        blurredBitmap = fastBlurBitmap(sourceBitmap, radius).asImageBitmap()
    }

    if (blurredBitmap != null) {
        this.then(
            Modifier.drawWithContent {
                drawIntoCanvas { canvas ->
                    drawImage(
                        image = blurredBitmap!!,
                        dstSize = IntSize(size.width.toInt(), size.height.toInt()),
                    )
                }
            }
        )
    } else {
        this
    }
}
