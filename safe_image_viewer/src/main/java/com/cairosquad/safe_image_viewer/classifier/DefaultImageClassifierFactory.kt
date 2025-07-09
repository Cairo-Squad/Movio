package com.cairosquad.safe_image_viewer.classifier

import org.tensorflow.lite.task.vision.classifier.ImageClassifier
import java.nio.ByteBuffer

internal class DefaultImageClassifierFactory : ImageClassifierFactory {
    override fun create(
        modelBuffer: ByteBuffer,
        options: ImageClassifier.ImageClassifierOptions
    ): ImageClassifier {
        return ImageClassifier.createFromBufferAndOptions(modelBuffer, options)
    }
}