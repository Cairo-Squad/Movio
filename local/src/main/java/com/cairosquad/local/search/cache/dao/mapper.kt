package com.cairosquad.local.search.cache.dao

import com.cairosquad.local.search.cache.entity.ArtistCacheEntity
import com.cairosquad.local.search.cache.entity.MovieCacheEntity
import com.cairosquad.local.search.cache.entity.SeriesCacheEntity
import com.cairosquad.repository.search.data_source.local.Dto.ArtistCacheDto
import com.cairosquad.repository.search.data_source.local.Dto.MovieCacheDto
import com.cairosquad.repository.search.data_source.local.Dto.SeriesCacheDto

fun MovieCacheDto.toEntity(): MovieCacheEntity {
    return MovieCacheEntity(
        id = id,
        title = title,
        posterPath = posterPath,
        voteAverage = voteAverage,
        query = query,
        timestamp = timestamp
    )
}

fun MovieCacheEntity.toDto(): MovieCacheDto {
    return MovieCacheDto(
        id = id,
        posterPath = posterPath,
        title = title,
        voteAverage = voteAverage,
        query = query,
        timestamp = timestamp
    )
}

fun SeriesCacheDto.toEntity(): SeriesCacheEntity {
    return SeriesCacheEntity(
        id = id,
        name = name,
        posterPath = posterPath,
        voteAverage = voteAverage,
        query = query,
        timestamp = timestamp
    )
}

fun SeriesCacheEntity.toDto(): SeriesCacheDto {
    return SeriesCacheDto(
        id = id,
        posterPath = posterPath,
        name = name,
        voteAverage = voteAverage,
        query = query,
        timestamp = timestamp
    )
}

fun ArtistCacheDto.toEntity(): ArtistCacheEntity {
    return ArtistCacheEntity(
        id = id,
        name = name,
        photoPath = photoPath,
        query = query,
        timestamp = timestamp
    )
}

fun ArtistCacheEntity.toDto(): ArtistCacheDto {
    return ArtistCacheDto(
        id = id,
        photoPath = photoPath,
        name = name,
        query = query,
        timestamp = timestamp
    )
}