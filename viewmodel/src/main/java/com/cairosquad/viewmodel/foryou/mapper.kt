package com.cairosquad.viewmodel.foryou

import com.cairosquad.entity.Movie
import com.cairosquad.viewmodel.util.localizeNumbers
import com.cairosquad.viewmodel.util.roundToFirstDecimalPlace

fun Movie.toUiState() = ForYouScreenState.MovieUiState(
    id = id,
    title = title.localizeNumbers(),
    rating = rating.roundToFirstDecimalPlace(),
    posterPath = posterPath
)