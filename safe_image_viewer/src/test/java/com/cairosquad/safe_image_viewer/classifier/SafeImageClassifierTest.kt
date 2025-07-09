package com.cairosquad.safe_image_viewer.classifier

import android.content.Context
import android.util.Log
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import org.junit.Before
import org.tensorflow.lite.support.common.FileUtil
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.label.Category
import org.tensorflow.lite.task.vision.classifier.Classifications
import org.tensorflow.lite.task.vision.classifier.ImageClassifier
import java.nio.MappedByteBuffer
import kotlin.test.Test

class SafeImageClassifierTest {
    private lateinit var context: Context
    private lateinit var factory: ImageClassifierFactory
    private lateinit var mockClassifier: ImageClassifier
    private lateinit var classifier: SafeImageClassifier

    @Before
    fun setup() {
        context = mockk(relaxed = true)
        mockClassifier = mockk()
        factory = mockk()

        mockkStatic(FileUtil::class)
        every { FileUtil.loadMappedFile(context, any()) } returns mockk<MappedByteBuffer>()

        every { factory.create(any(), any()) } returns mockClassifier

        classifier = SafeImageClassifier(context, factory)
    }

    @Test
    fun `returns true for safe image`() {
        val tensorImage = mockk<TensorImage>()
        mockkStatic(TensorImage::class)
        every { TensorImage.fromBitmap(any()) } returns tensorImage

        val categoryNonNude = mockk<Category>()
        val categoryNude = mockk<Category>()
        every { categoryNonNude.score } returns 0.9f
        every { categoryNude.score } returns 0.05f

        val classification = mockk<Classifications>()
        every { classification.categories } returns listOf(categoryNonNude, categoryNude)
        every { mockClassifier.classify(tensorImage) } returns listOf(classification)

        val result = classifier.isInappropriate(
            mockk(relaxed = true),
            nsfwThreshold = 0.5,
            sfwThreshold = 0.8,
            isLogEnabled = false
        )

        assertThat(result).isTrue()
    }

    @Test
    fun `returns false for inappropriate image`() {
        val tensorImage = mockk<TensorImage>()
        mockkStatic(TensorImage::class)
        every { TensorImage.fromBitmap(any()) } returns tensorImage

        val categoryNonNude = mockk<Category>()
        val categoryNude = mockk<Category>()
        every { categoryNonNude.score } returns 0.6f
        every { categoryNude.score } returns 0.7f

        val classification = mockk<Classifications>()
        every { classification.categories } returns listOf(categoryNonNude, categoryNude)
        every { mockClassifier.classify(tensorImage) } returns listOf(classification)

        val result = classifier.isInappropriate(
            mockk(relaxed = true), nsfwThreshold = 0.6, sfwThreshold = 0.85, isLogEnabled = false
        )

        assertThat(result).isFalse()
    }

    @Test
    fun `returns false when nonNudeScore is below threshold but nudeScore is safe`() {
        val tensorImage = mockk<TensorImage>()
        mockkStatic(TensorImage::class)
        every { TensorImage.fromBitmap(any()) } returns tensorImage

        val categoryNonNude = mockk<Category>()
        val categoryNude = mockk<Category>()
        every { categoryNonNude.score } returns 0.6f   // < sfwThreshold
        every { categoryNude.score } returns 0.3f     // < nsfwThreshold

        val classification = mockk<Classifications>()
        every { classification.categories } returns listOf(categoryNonNude, categoryNude)
        every { mockClassifier.classify(tensorImage) } returns listOf(classification)

        val result = classifier.isInappropriate(
            mockk(relaxed = true), nsfwThreshold = 0.5, sfwThreshold = 0.9, isLogEnabled = false
        )

        assertThat(result).isFalse()
    }

    @Test
    fun `returns false when nonNudeScore is high but nudeScore is too high`() {
        val tensorImage = mockk<TensorImage>()
        mockkStatic(TensorImage::class)
        every { TensorImage.fromBitmap(any()) } returns tensorImage

        val categoryNonNude = mockk<Category>()
        val categoryNude = mockk<Category>()
        every { categoryNonNude.score } returns 0.95f   // >= sfwThreshold
        every { categoryNude.score } returns 0.7f       // >= nsfwThreshold

        val classification = mockk<Classifications>()
        every { classification.categories } returns listOf(categoryNonNude, categoryNude)
        every { mockClassifier.classify(tensorImage) } returns listOf(classification)

        val result = classifier.isInappropriate(
            mockk(relaxed = true), nsfwThreshold = 0.6, sfwThreshold = 0.9, isLogEnabled = false
        )

        assertThat(result).isFalse()
    }

    @Test
    fun `logs scores when isLogEnabled is true`() {
        val tensorImage = mockk<TensorImage>()
        mockkStatic(TensorImage::class)
        every { TensorImage.fromBitmap(any()) } returns tensorImage

        val categoryNonNude = mockk<Category>()
        val categoryNude = mockk<Category>()
        every { categoryNonNude.score } returns 0.8f
        every { categoryNude.score } returns 0.2f

        val classification = mockk<Classifications>()
        every { classification.categories } returns listOf(categoryNonNude, categoryNude)
        every { mockClassifier.classify(tensorImage) } returns listOf(classification)

        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0

        classifier.isInappropriate(
            mockk(relaxed = true), nsfwThreshold = 0.5, sfwThreshold = 0.7, isLogEnabled = true
        )

        verify {
            Log.d("Safe Image Viewer", match { it.contains("nude image score") })
        }
    }
}