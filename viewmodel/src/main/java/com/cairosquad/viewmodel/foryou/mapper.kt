package com.cairosquad.viewmodel.foryou

import com.cairosquad.entity.Movie

fun Movie.toUiState() = ForYouState.MovieUiState(
    id = id,
    title = title,
    rating = rating / 2,
    posterPath = posterPath
)