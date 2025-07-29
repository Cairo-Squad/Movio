package com.cairosquad.repository.series.data_source.remote

import com.cairosquad.entity.Genre
import com.cairosquad.entity.Series
import com.cairosquad.repository.series.data_source.remote.dto.SeriesRemoteDto
import com.cairosquad.repository.utils.TimeUtils

fun SeriesRemoteDto.toEntity(allGenres: List<Genre> = emptyList()): Series {
    return Series(
        id = id ?: 0L,
        title = name ?: "",
        rating = voteAverage?.times(0.5f) ?: 0f,
        posterPath = posterPath ?: "",
        trailerPath = "",
        genres = allGenres.filter { genreIds?.contains(it.id) == true },
        overview = overview ?: "",
        releaseDate = releaseDate?.let { TimeUtils.dateToLong(it)  } ?: 0L,
        seasonsCount = 1,
    )
}

@JvmName("toEntitySeries")
fun List<SeriesRemoteDto>.toEntityList(): List<Series> {
    return map { it.toEntity() }
}
