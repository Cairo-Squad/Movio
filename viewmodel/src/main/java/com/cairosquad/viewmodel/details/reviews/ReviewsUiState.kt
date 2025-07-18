package com.cairosquad.viewmodel.details.reviews

data class ReviewsScreenState(
    val reviews: List<ReviewUiState> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
) {
    data class ReviewUiState(
        val reviewerName: String,
        val reviewDate: String,
        val rating: String,
        val reviewText: String,
        val reviewerImageUrl: String? = null,
    )

}
