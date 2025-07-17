package com.cairosquad.viewmodel.details.reviews

import com.cairosquad.entity.Review

fun Review.toUiState() = ReviewsScreenState.ReviewUiState(
    reviewerName = author,
    reviewDate = date.toString(),
    rating = rating ,
    reviewText = description ,
    reviewerImageUrl = authorPhotoPath
)