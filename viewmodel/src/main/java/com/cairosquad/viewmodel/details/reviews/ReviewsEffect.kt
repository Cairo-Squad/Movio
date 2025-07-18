package com.cairosquad.viewmodel.details.reviews

import com.cairosquad.viewmodel.exception.ErrorStatus

sealed class ReviewsEffect {
    object NavigateBack : ReviewsEffect()
    data class ErrorHappened(val message: ErrorStatus) : ReviewsEffect()
}