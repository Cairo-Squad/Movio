package com.cairosquad.entity

data class Movie(
    val id: Long,
    val title: String,
    val rating: Float,
    val posterPath: String,
    val genres: List<Genre>,
    val overview: String,
    val releaseDate: Long,
    val runtimeMinutes: Int,
    val trailerPath: String,
)
