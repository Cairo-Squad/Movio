package com.cairosquad.safe_image_viewer.classifier

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.Immutable
import org.tensorflow.lite.support.common.FileUtil
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.vision.classifier.ImageClassifier

@Immutable
internal class SafeImageClassifier(
    context: Context,
    factory: ImageClassifierFactory = DefaultImageClassifierFactory()
) {
    private val imageClassifier: ImageClassifier

    init {
        val baseOptions = BaseOptions.builder()
            .setNumThreads(NUMBER_OF_THREADS)
            .build()

        val options = ImageClassifier.ImageClassifierOptions.builder()
            .setBaseOptions(baseOptions)
            .setMaxResults(MAX_RESULTS)
            .build()

        val modelBuffer = FileUtil.loadMappedFile(context, MODEL)
        imageClassifier = factory.create(modelBuffer, options)
    }

    fun isInappropriate(
        bitmap: Bitmap,
        nsfwThreshold: Double,
        sfwThreshold: Double,
        isLogEnabled: Boolean
    ): Boolean {
        val tensorImage = TensorImage.fromBitmap(bitmap)
        val results = imageClassifier.classify(tensorImage)[0]

        val nonNudeScore = results.categories[NON_NUDE_INDEX].score
        val nudeScore = results.categories[NUDE_INDEX].score

        if (isLogEnabled) {
            Log.d(TAG, "nude image score: $nudeScore || non nude image score: $nonNudeScore")
        }

        return nonNudeScore >= sfwThreshold && nudeScore < nsfwThreshold
    }

    companion object {
        private const val TAG = "Safe Image Viewer"
        private const val MODEL = "nsfw.tflite"
        private const val NON_NUDE_INDEX = 0
        private const val NUDE_INDEX = 1
        private const val NUMBER_OF_THREADS = 2
        private const val MAX_RESULTS = 2
    }
}