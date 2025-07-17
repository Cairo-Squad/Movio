package com.cairosquad.repository.search.data_source.local.dto

import com.cairosquad.entity.Artist
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series
import java.util.Date

fun Series.toCacheDto(): SeriesCacheDto {
    return SeriesCacheDto(
        id = id.toInt(),
        name = title,
        posterPath = posterPath,
        voteAverage = rating.toDouble(),
        timestamp = Date().time
    )
}

@JvmName("toCacheSeriesDto")
fun List<Series>.toCacheDto(): List<SeriesCacheDto> {
    return map { it.toCacheDto() }
}

fun SeriesCacheDto.toEntity(): Series {
    return Series(
        id = id.toLong(),
        title = name ?: "",
        posterPath = posterPath ?: "",
        rating = voteAverage?.toFloat() ?: 0f,
    )
}

@JvmName("toEntitySeries")
fun List<SeriesCacheDto>.toEntity(): List<Series> {
    return map { it.toEntity() }
}

fun Movie.toCacheDto(): MovieCacheDto {
    return MovieCacheDto(
        id = id.toInt(),
        title = title,
        posterPath = posterPath,
        voteAverage = rating.toDouble(),
        timestamp = Date().time
    )
}

@JvmName("toCacheMovieDto")
fun List<Movie>.toCacheDto(): List<MovieCacheDto> {
    return map { it.toCacheDto() }
}

fun MovieCacheDto.toEntity(): Movie {
    return Movie(
        id = id.toLong(),
        title = title ?: "",
        posterPath = posterPath ?: "",
        rating = voteAverage?.toFloat() ?: 0f
    )
}

@JvmName("toEntityMovie")
fun List<MovieCacheDto>.toEntity(): List<Movie> {
    return map { it.toEntity() }
}

fun Artist.toCacheDto(): ArtistCacheDto {
    return ArtistCacheDto(
        id = id.toInt(),
        name = name,
        photoPath = photoPath,
        timestamp = Date().time
    )
}

@JvmName("toCacheArtistDto")
fun List<Artist>.toCacheDto(): List<ArtistCacheDto> {
    return map { it.toCacheDto() }
}

fun ArtistCacheDto.toEntity(): Artist {
    return Artist(
        id = id.toLong(),
        name = name ?: "",
        photoPath = photoPath ?: "",
    )
}

@JvmName("toEntityArtist")
fun List<ArtistCacheDto>.toEntity(): List<Artist> {
    return map { it.toEntity() }
}