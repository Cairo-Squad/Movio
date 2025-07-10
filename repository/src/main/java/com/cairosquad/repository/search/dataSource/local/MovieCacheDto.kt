package com.cairosquad.repository.search.dataSource.local.Dto

import com.cairosquad.entity.Movie

data class MovieCacheDto(
    val id: Int = 0,
    val title: String?,
    val voteAverage: Double?,
    val posterPath: String?
) {
    fun toMovie(): Movie {
        return Movie(
            id = id.toLong(),
            title = title ?: "",
            posterPath = posterPath ?: "",
            rating = voteAverage?.toFloat() ?: 0f
        )
    }
}
