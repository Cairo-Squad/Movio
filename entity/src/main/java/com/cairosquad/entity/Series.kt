package com.cairosquad.entity

data class Series(
    val id: Long,
    val title: String,
    val rating: Float,
    val posterPath: String,
    val trailerPath: String,
    val genres: List<Genre>,
    val overview: String,
    val releaseDate: Long,
    val seasonsCount: Int,
)