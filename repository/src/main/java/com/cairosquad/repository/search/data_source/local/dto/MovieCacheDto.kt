package com.cairosquad.repository.search.data_source.local.Dto

data class MovieCacheDto(
    val id: Int = 0,
    val title: String?,
    val voteAverage: Double?,
    val posterPath: String?,
    val query: String,
    val timestamp: Long
)
