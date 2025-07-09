package com.cairosquad.safe_image_viewer.classifier

import org.tensorflow.lite.task.vision.classifier.ImageClassifier
import java.nio.ByteBuffer

interface ImageClassifierFactory {
    fun create(
        modelBuffer: ByteBuffer,
        options: ImageClassifier.ImageClassifierOptions
    ): ImageClassifier
}