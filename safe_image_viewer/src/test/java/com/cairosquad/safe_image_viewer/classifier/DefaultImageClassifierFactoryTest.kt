package com.cairosquad.safe_image_viewer.classifier

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import org.junit.Before
import org.tensorflow.lite.task.vision.classifier.ImageClassifier
import java.nio.ByteBuffer
import kotlin.test.Test

class DefaultImageClassifierFactoryTest {

    @Before
    fun setup() {
        mockkStatic(ImageClassifier::class)
    }

    @Test
    fun `create should return ImageClassifier from buffer and options`() {
        // given
        val buffer = mockk<ByteBuffer>()
        val options = mockk<ImageClassifier.ImageClassifierOptions>()
        val expectedClassifier = mockk<ImageClassifier>()

        every {
            ImageClassifier.createFromBufferAndOptions(buffer, options)
        } returns expectedClassifier

        val factory = DefaultImageClassifierFactory()

        // when
        val result = factory.create(buffer, options)

        // then
        assertThat(result).isEqualTo(expectedClassifier)

        verify { ImageClassifier.createFromBufferAndOptions(buffer, options) }
    }
}
