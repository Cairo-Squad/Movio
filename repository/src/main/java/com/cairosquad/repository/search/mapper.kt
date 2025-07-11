package com.cairosquad.repository.search

import com.cairosquad.entity.Artist
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series
import com.cairosquad.repository.search.data_source.local.Dto.ArtistCacheDto
import com.cairosquad.repository.search.data_source.local.Dto.MovieCacheDto
import com.cairosquad.repository.search.data_source.local.Dto.SeriesCacheDto
import java.time.Instant

fun Series.toSeriesCacheDto(query: String): SeriesCacheDto {
    return SeriesCacheDto(
        id = id.toInt(),
        name = title,
        posterPath = posterPath,
        voteAverage = rating.toDouble(),
        query = query,
        timestamp = Instant.now().toEpochMilli()
    )
}

fun SeriesCacheDto.toSeries(): Series {
    return Series(
        id = id.toLong(),
        title = name ?: "",
        posterPath = posterPath ?: "",
        rating = voteAverage?.toFloat() ?: 0f,
    )
}

fun Movie.toMovieCacheDto(query: String): MovieCacheDto {
    return MovieCacheDto(
        id = id.toInt(),
        title = title,
        posterPath = posterPath,
        voteAverage = rating.toDouble(),
        query = query,
        timestamp = Instant.now().toEpochMilli()
    )
}


fun MovieCacheDto.toMovie(): Movie {
    return Movie(
        id = id.toLong(),
        title = title ?: "",
        posterPath = posterPath ?: "",
        rating = voteAverage?.toFloat() ?: 0f
    )
}

fun Artist.toArtistCacheDto(query: String): ArtistCacheDto {
    return ArtistCacheDto(
        id = id.toInt(),
        name = name,
        photoPath = photoPath,
        query = query,
        timestamp = Instant.now().toEpochMilli()
    )
}

fun ArtistCacheDto.toArtist(): Artist {
    return Artist(
        id = id.toLong(),
        name = name ?: "",
        photoPath = photoPath ?: "",
    )
}