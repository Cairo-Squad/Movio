package com.cairosquad.repository.search.data_source.local.dto

import com.cairosquad.entity.Artist
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series
import java.time.Instant

fun Series.toCacheDto(query: String): SeriesCacheDto {
    return SeriesCacheDto(
        id = id.toInt(),
        name = title,
        posterPath = posterPath,
        voteAverage = rating.toDouble(),
        query = query,
        timestamp = Instant.now().toEpochMilli()
    )
}

@JvmName("toCacheSeriesDto")
fun List<Series>.toCacheDto(query: String): List<SeriesCacheDto> {
    return map { it.toCacheDto(query) }
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

fun Movie.toCacheDto(query: String,page:Int): MovieCacheDto {
    return MovieCacheDto(
        id = id.toInt(),
        page = page,
        title = title,
        posterPath = posterPath,
        voteAverage = rating.toDouble(),
        query = query,
        timestamp = Instant.now().toEpochMilli()
    )
}

@JvmName("toCacheMovieDto")
fun List<Movie>.toCacheDto(query: String,page: Int): List<MovieCacheDto> {
    return map { it.toCacheDto(query,page) }
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

fun Artist.toCacheDto(query: String): ArtistCacheDto {
    return ArtistCacheDto(
        id = id.toInt(),
        name = name,
        photoPath = photoPath,
        query = query,
        timestamp = Instant.now().toEpochMilli()
    )
}

@JvmName("toCacheArtistDto")
fun List<Artist>.toCacheDto(query: String): List<ArtistCacheDto> {
    return map { it.toCacheDto(query) }
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