package com.cairosquad.viewmodel.details.reviews

import com.cairosquad.entity.Review
import com.cairosquad.viewmodel.util.localizeNumbers
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Review.toUiState() = ReviewsScreenState.ReviewUiState(
    reviewerName = author.localizeNumbers(),
    reviewDate = Timestamp(date).toDateFormat(),
    rating = String.format(Locale.getDefault(), "%.1f", rating),
    reviewText = description.localizeNumbers(),
    reviewerImageUrl = authorPhotoPath
)

@JvmInline
private value class Timestamp(val value: Long)

private fun Timestamp.toDateFormat(): String {
    val date = Date(this.value)
    val format = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
    return format.format(date)
}