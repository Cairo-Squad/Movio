package com.cairosquad.repository.search.data_source.local.dto

import com.cairosquad.entity.Artist
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series
import java.time.Instant

fun Series.toCacheDto(query: String): CachedSeriesDto {
    return CachedSeriesDto(
        id = id.toInt(),
        name = title,
        posterPath = posterPath,
        voteAverage = rating.toDouble(),
        query = query,
        timestamp = Instant.now().toEpochMilli()
    )
}

fun CachedSeriesDto.toEntity(): Series {
    return Series(
        id = id.toLong(),
        title = name ?: "",
        posterPath = posterPath ?: "",
        rating = voteAverage?.toFloat() ?: 0f,
    )
}

fun Movie.toCacheDto(query: String): CachedMovieDto {
    return CachedMovieDto(
        id = id.toInt(),
        title = title,
        posterPath = posterPath,
        voteAverage = rating.toDouble(),
        query = query,
        timestamp = Instant.now().toEpochMilli()
    )
}

fun CachedMovieDto.toEntity(): Movie {
    return Movie(
        id = id.toLong(),
        title = title ?: "",
        posterPath = posterPath ?: "",
        rating = voteAverage?.toFloat() ?: 0f
    )
}

fun Artist.toCacheDto(query: String): CachedArtistDto {
    return CachedArtistDto(
        id = id.toInt(),
        name = name,
        photoPath = photoPath,
        query = query,
        timestamp = Instant.now().toEpochMilli()
    )
}

fun CachedArtistDto.toEntity(): Artist {
    return Artist(
        id = id.toLong(),
        name = name ?: "",
        photoPath = photoPath ?: "",
    )
}
