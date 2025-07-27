package com.cairosquad.repository.series.data_source.remote

import com.cairosquad.entity.Genre
import com.cairosquad.entity.Series
import com.cairosquad.repository.series.data_source.remote.dto.SeriesRemoteDto
import java.text.SimpleDateFormat
import java.util.Locale

fun SeriesRemoteDto.toEntity(allGenres: List<Genre> = emptyList()): Series {
    return Series(
        id = id ?: 0L,
        title = name ?: "",
        rating = voteAverage ?: 0f,
        posterPath = posterPath ?: "",
        trailerPath = "",
        genres = allGenres.filter { genreIds?.contains(it.id) == true },
        overview = overview ?: "",
        releaseDate = releaseDate?.let { parseDateToMillis(it)  } ?: 0L,
        seasonsCount = 1,
    )
}

@JvmName("toEntitySeries")
fun List<SeriesRemoteDto>.toEntityList(): List<Series> {
    return map { it.toEntity() }
}

private fun parseDateToMillis(dateStr: String): Long {
    return try {
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        formatter.parse(dateStr)?.time ?: 0L
    } catch (e: Exception) {
        0L
    }
}