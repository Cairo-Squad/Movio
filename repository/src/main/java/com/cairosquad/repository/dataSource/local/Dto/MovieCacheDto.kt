package com.cairosquad.repository.dataSource.local.Dto

data class MovieCacheDto(
    val id: Int = 0,
    val title: String?,
    val voteAverage: Double?,
    val posterPath: String?
)
