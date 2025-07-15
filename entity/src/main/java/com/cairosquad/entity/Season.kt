package com.cairosquad.entity

data class Season(
    val seasonNumber: Int,
    val seasonName: String,
    val seriesId: Long,
    val episodesCount: Int,
    val rating: Float,
    val posterPath: String,
    val overview: String,
    val airDate: Long
)
