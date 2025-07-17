package com.cairosquad.viewmodel.details.reviews

import com.cairosquad.entity.Review
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Review.toUiState() = ReviewsScreenState.ReviewUiState(
    reviewerName = author,
    reviewDate = Timestamp(date).toDateFormat(),
    rating = rating.toSingleDecimal(),
    reviewText = description,
    reviewerImageUrl = authorPhotoPath
)

@JvmInline
value class Timestamp(val value: Long)

fun Timestamp.toDateFormat(): String {
    val date = Date(this.value)
    val format = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
    return format.format(date)
}

private fun String.toSingleDecimal(): String {
    return this.toDoubleOrNull()
        ?.let { String.format(Locale.getDefault(), "%.1f", it) }
        ?: "0.0"
}
