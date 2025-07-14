package com.cairosquad.entity

data class Movie(
    val id: Long,
    val title: String,
    val rating: Float,
    val posterPath: String,
    val genres: List<Genre> = emptyList(), // TODO: remove default value
    val overview: String = "", // TODO: remove default value
    val releaseDate: Long = 0, // TODO: remove default value
    val runtimeMinutes: Int = 1, // TODO: remove default value
)
