package com.cairosquad.repository.search.dataSource.local.Dto

import com.cairosquad.entity.Series

data class SeriesCacheDto(
    val id: Int = 0,
    val name: String? = null,
    val posterPath: String? = null,
    val voteAverage: Double? = null
) {
    fun toSeries(): Series {
        return Series(
            id = id.toLong(),
            title = name ?: "",
            posterPath = posterPath ?: "",
            rating = voteAverage?.toFloat() ?: 0f
        )
    }
}
