package com.cairosquad.repository.search

import com.cairosquad.entity.Artist
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series
import com.cairosquad.repository.search.dataSource.local.Dto.ArtistCacheDto
import com.cairosquad.repository.search.dataSource.local.Dto.MovieCacheDto
import com.cairosquad.repository.search.dataSource.local.Dto.SeriesCacheDto

fun Series.toSeriesCacheDto(): SeriesCacheDto {
    return SeriesCacheDto(
        id = id.toInt(),
        name = title,
        posterPath = posterPath,
        voteAverage = rating.toDouble()
    )
}

fun Movie.toMovieCacheDto(): MovieCacheDto {
    return MovieCacheDto(
        id = id.toInt(),
        title = title,
        posterPath = posterPath,
        voteAverage = rating.toDouble()
    )
}

fun Artist.toArtistCacheDto(): ArtistCacheDto {
    return ArtistCacheDto(
        id = id.toInt(),
        name = name,
        photoPath = photoPath,
    )
}