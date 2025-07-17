package com.cairosquad.repository.search.data_source.remote.dto

import com.cairosquad.entity.Artist
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series
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

fun MovieRemoteDto.toEntity(): Movie {
    return Movie(
        id = id?.toLong() ?: 0L,
        title = title ?: "",
        rating = voteAverage?.toFloat() ?: 0f,
        posterPath = posterPath ?: "",
    )
}

@JvmName("toEntityMovie")
fun List<MovieRemoteDto>.toEntityList(): List<Movie> {
    return map { it.toEntity() }
}

fun SeriesRemoteDto.toEntity(): Series {
    return Series(
        id = id ?: 0L,
        title = name ?: "",
        rating = voteAverage?.toFloat() ?: 0f,
        posterPath = posterPath ?: "",
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