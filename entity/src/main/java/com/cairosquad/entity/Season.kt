package com.cairosquad.entity

data class Season(
    val seriesId: Long,
    val seasonNumber: Int,
    val seasonName: String,
    val episodesCount: Int,
    val rating: Float,
    val posterPath: String,
    val overview: String,
    val airDate: Long
)
