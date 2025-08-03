package com.cairosquad.safe_image_viewer.classifier

import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.util.LruCache
import androidx.core.graphics.scale
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.automl.FirebaseAutoMLLocalModel
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel
import com.google.firebase.ml.vision.label.FirebaseVisionOnDeviceAutoMLImageLabelerOptions
import java.util.concurrent.Executors

object NSFWDetector {
	private const val TAG = "NSFWDetector"
	private const val LABEL_SFW = "nude"
	private const val LABEL_NSFW = "nonnude"
	private const val NUMBER_OF_THREADS = 10
	private const val NUMBER_OF_CACHES_IMAGES = 100

	// Lazy initialization - only create when first used
	private val localModel by lazy {
		FirebaseAutoMLLocalModel.Builder()
			.setAssetFilePath("manifest.json")
			.build()
	}

	private val options by lazy {
		FirebaseVisionOnDeviceAutoMLImageLabelerOptions
			.Builder(localModel)
			.setConfidenceThreshold(0.1f)
			.build()
	}

	private val interpreter by lazy {
		FirebaseVision.getInstance()
			.getOnDeviceAutoMLImageLabeler(options)
	}

	private val imageCache = LruCache<String, Boolean>(NUMBER_OF_CACHES_IMAGES)

	private val executorService = Executors.newFixedThreadPool(NUMBER_OF_THREADS)

	fun isNSFWCancellable(
		bitmap: Bitmap,
		nudeThreshold: Double,
		nonNudeThreshold: Double,
		imageUrl: String,
		enableLog: Boolean = false,
		isActive: () -> Boolean,
		callback: (isNSFW: Boolean) -> Unit
	) {
		// Early exit if not active
		if (! isActive()) {
			if (enableLog) Log.d(TAG, "Processing cancelled - not active")
			return
		}

		// Generate cache key based on bitmap properties
		val cacheKey = "cached image: $imageUrl"

		// Check cache first
		imageCache.get(cacheKey)?.let { cachedResult ->
			if (isActive()) {
				if (enableLog) Log.d(TAG, "Cache hit for image")
				callback(cachedResult)
			}
			return
		}

		// Check if still active before expensive operations
		if (! isActive()) return

		// Optimize bitmap for processing
		val optimizedBitmap = optimizeBitmap(bitmap)

		// Check again before Firebase call
		if (! isActive()) return

		val image = FirebaseVisionImage.fromBitmap(optimizedBitmap)

		interpreter.processImage(image)
			.addOnSuccessListener { labels ->
				// Check if still active before processing
				if (! isActive()) {
					if (enableLog) Log.d(TAG, "Processing cancelled after Firebase success")
					return@addOnSuccessListener
				}

				executorService.execute {
					try {
						// Final activity check before processing results
						if (! isActive()) {
							if (enableLog) Log.d(
								TAG,
								"Processing cancelled during label processing"
							)
							return@execute
						}

						val result =
							processLabels(labels, nudeThreshold, nonNudeThreshold, enableLog)

						imageCache.put(cacheKey, result)

						// Return result on main thread
						Handler(Looper.getMainLooper()).post {
							if (isActive()) {
								callback(result)
							}
						}

					} catch (e: Exception) {
						if (isActive()) {
							Log.e(TAG, "Error processing labels: ${e.localizedMessage}")
							Handler(Looper.getMainLooper()).post {
								if (isActive()) {
									callback(false)
								}
							}
						}
					}
				}
			}
			.addOnFailureListener { e ->
				if (isActive()) {
					Log.e(TAG, "NSFW detection failed: ${e.localizedMessage}")
					callback(false)
				}
			}
	}

	private fun processLabels(
		labels: List<FirebaseVisionImageLabel>,
		nudeThreshold: Double,
		nonNudeThreshold: Double,
		enableLog: Boolean
	): Boolean {
		if (labels.isEmpty()) {
			if (enableLog) Log.d(TAG, "No labels detected")
			return false
		}

		// Process all relevant labels, not just the first one
		var maxNudeConfidence = 0f
		var maxNonNudeConfidence = 0f

		for (label in labels) {
			if (enableLog) {
				Log.d(TAG, "Label: ${label.text}, Confidence: ${label.confidence}")
			}

			when (label.text) {
				LABEL_SFW -> {
					maxNudeConfidence = maxOf(maxNudeConfidence, label.confidence)
				}

				LABEL_NSFW -> {
					maxNonNudeConfidence = maxOf(maxNonNudeConfidence, label.confidence)
				}
			}
		}

		// Improved logic: compare both confidences
		return when {
			maxNudeConfidence > nudeThreshold -> true
			maxNonNudeConfidence > nonNudeThreshold -> false
			maxNudeConfidence > maxNonNudeConfidence -> true
			else -> false
		}
	}

	private fun optimizeBitmap(bitmap: Bitmap): Bitmap {
		// Resize if too large (ML models work better with smaller, consistent sizes)
		val maxSize = 512
		return if (bitmap.width > maxSize || bitmap.height > maxSize) {
			val ratio = minOf(
				maxSize.toFloat() / bitmap.width,
				maxSize.toFloat() / bitmap.height
			)
			val newWidth = (bitmap.width * ratio).toInt()
			val newHeight = (bitmap.height * ratio).toInt()

			bitmap.scale(newWidth, newHeight)
		} else {
			bitmap
		}
	}
}