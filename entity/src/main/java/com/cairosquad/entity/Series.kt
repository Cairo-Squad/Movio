package com.cairosquad.entity

data class Series(
    val id: Long,
    val title: String,
    val rating: Float,
    val posterPath: String,
    val genres: List<Genre>,
)