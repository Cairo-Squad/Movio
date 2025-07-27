package com.cairosquad.repository.movie.data_source.remote

import com.cairosquad.entity.Genre
import com.cairosquad.entity.Movie
import com.cairosquad.repository.movie.data_source.remote.dto.MovieRemoteDto
import java.text.SimpleDateFormat
import java.util.Locale

fun MovieRemoteDto.toEntity(allGenres: List<Genre> = emptyList()): Movie {
    return Movie(
        id = id?.toLong() ?: 0L,
        title = title ?: "",
        rating = voteAverage?.toFloat() ?: 0f,
        posterPath = posterPath ?: "",
        genres = allGenres.filter { genreIds?.contains(it.id) == true },
        overview = overview.orEmpty(),
        releaseDate = releaseDate?.let { parseDateToMillis(it) } ?: 0L,
        runtimeMinutes = 0,
        trailerPath = "",
    )
}

@JvmName("toEntityMovie")
fun List<MovieRemoteDto>.toEntityList(): List<Movie> {
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