package com.cairosquad.viewmodel.rated

data class RatedItemUiState(
    val id: Long,
    val title: String,
    val posterPath: String?,
    val releaseDate: String?,
    val rating: Double,
    val isMovie: Boolean
)
