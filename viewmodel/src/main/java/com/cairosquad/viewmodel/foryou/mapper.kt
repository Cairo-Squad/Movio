package com.cairosquad.viewmodel.foryou

import com.cairosquad.entity.Movie
import com.cairosquad.viewmodel.util.roundToFirstDecimalPlace

fun Movie.toUiState() = ForYouState.MovieUiState(
    id = id,
    title = title,
    rating = rating.roundToFirstDecimalPlace(),
    posterPath = posterPath
)