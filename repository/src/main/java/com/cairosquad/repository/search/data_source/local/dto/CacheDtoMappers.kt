package com.cairosquad.repository.search.data_source.local.dto

import com.cairosquad.entity.Artist
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series
import java.util.Date

fun Series.toCacheDto(query: String,page: Int): SeriesCacheDto {
    return SeriesCacheDto(
        id = id.toInt(),
        page = page,
        name = title,
        posterPath = posterPath,
        voteAverage = rating.toDouble(),
        query = query,
        timestamp = Date().time
    )
}

@JvmName("toCacheSeriesDto")
fun List<Series>.toCacheDto(query: String,page: Int): List<SeriesCacheDto> {
    return map { it.toCacheDto(query,page) }
}

fun SeriesCacheDto.toEntity(): Series {
    return Series(
        id = id.toLong(),
        title = name ?: "",
        posterPath = posterPath ?: "",
        rating = voteAverage?.toFloat() ?: 0f,
        trailerPath = "",
        genres = emptyList(),
        overview = "",
        releaseDate = 0L,
        seasonsCount = 1,
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
        timestamp = Date().time
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

fun Artist.toCacheDto(query: String,page: Int): ArtistCacheDto {
    return ArtistCacheDto(
        id = id.toInt(),
        page =page ,
        name = name,
        photoPath = photoPath,
        query = query,
        timestamp = Date().time
    )
}

@JvmName("toCacheArtistDto")
fun List<Artist>.toCacheDto(query: String,page: Int): List<ArtistCacheDto> {
    return map { it.toCacheDto(query,page) }
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