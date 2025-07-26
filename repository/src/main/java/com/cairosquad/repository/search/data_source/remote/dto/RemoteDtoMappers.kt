package com.cairosquad.repository.search.data_source.remote.dto

import com.cairosquad.entity.Artist
import com.cairosquad.entity.Genre
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series
import com.cairosquad.repository.movie.data_source.remote.dto.MovieRemoteDto
import com.cairosquad.repository.series.data_source.remote.dto.SeriesRemoteDto
import java.time.LocalDate
import java.time.ZoneOffset


fun ArtistRemoteDto.toEntity(): Artist {
    return Artist(
        id = id?.toLong() ?: 0L,
        name = name ?: "",
        photoPath = profilePath ?: "",
        country = placeOfBirth ?: "",
        birthDate = birthday?.toMillisFromDate() ?: 0,
        biography = biography ?: "",
        department = department ?: "",
    )
}

@JvmName("toEntityArtist")
fun List<ArtistRemoteDto>.toEntityList(): List<Artist> {
    return map { it.toEntity() }
}

fun MovieRemoteDto.toEntity(allGenres: List<Genre> = emptyList()): Movie {
    return Movie(
        id = id?.toLong() ?: 0L,
        title = title ?: "",
        rating = voteAverage?.toFloat() ?: 0f,
        posterPath = posterPath ?: "",
        genres = allGenres.filter { genreIds?.contains(it.id) == true },
        overview = overview.orEmpty(),
        releaseDate = releaseDate?.let { parseDateToMillis(it)  } ?: 0L,
        runtimeMinutes = 0,
        trailerPath = "",
    )
}

@JvmName("toEntityMovie")
fun List<MovieRemoteDto>.toEntityList(): List<Movie> {
    return map { it.toEntity() }
}

fun SeriesRemoteDto.toEntity(allGenres: List<Genre> = emptyList()): Series {
    return Series(
        id = id ?: 0L,
        title = name ?: "",
        rating = voteAverage?.toFloat() ?: 0f,
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

private fun String.toMillisFromDate(): Long {
    val localDate = LocalDate.parse(this) // Assumes input format yyyy-mm-dd
    return localDate.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
}
private fun parseDateToMillis(dateStr: String): Long {
    return try {
        val formatter = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
        formatter.parse(dateStr)?.time ?: 0L
    } catch (e: Exception) {
        0L
    }
}