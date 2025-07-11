package com.cairosquad.repository.search.data_source.local.Dto

data class SeriesCacheDto(
    val id: Int = 0,
    val name: String? = null,
    val posterPath: String? = null,
    val voteAverage: Double? = null,
    val query: String,
    val timestamp: Long
)