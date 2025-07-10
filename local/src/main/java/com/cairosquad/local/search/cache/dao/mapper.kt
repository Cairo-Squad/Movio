package com.cairosquad.local.search.cache.dao

import com.cairosquad.local.search.cache.entity.ArtistCacheEntity
import com.cairosquad.local.search.cache.entity.MovieCacheEntity
import com.cairosquad.local.search.cache.entity.SeriesCacheEntity
import com.cairosquad.repository.search.data_source.local.Dto.ArtistCacheDto
import com.cairosquad.repository.search.data_source.local.Dto.MovieCacheDto
import com.cairosquad.repository.search.data_source.local.Dto.SeriesCacheDto

fun MovieCacheDto.toEntity(
    query: String,
    timestamp: Long
): MovieCacheEntity {
    return MovieCacheEntity(
        id = this.id,
        title = this.title,
        posterPath = this.posterPath,
        voteAverage = this.voteAverage,
        query = query,
        timestamp = timestamp
    )
}

fun MovieCacheEntity.toDto(): MovieCacheDto {
    return MovieCacheDto(
        id = this.id,
        posterPath = this.posterPath,
        title = this.title,
        voteAverage = this.voteAverage,
    )
}

fun SeriesCacheDto.toEntity(
    query: String,
    timestamp: Long
): SeriesCacheEntity {
    return SeriesCacheEntity(
        id = this.id,
        name = this.name,
        posterPath = this.posterPath,
        voteAverage = this.voteAverage,
        query = query,
        timestamp = timestamp
    )
}

fun SeriesCacheEntity.toDto(): SeriesCacheDto {
    return SeriesCacheDto(
        id = this.id,
        posterPath = this.posterPath,
        name = this.name,
        voteAverage = this.voteAverage,
    )
}

fun ArtistCacheDto.toEntity(
    query: String,
    timestamp: Long
): ArtistCacheEntity {
    return ArtistCacheEntity(
        id = this.id,
        name = this.name,
        photoPath = this.photoPath,
        query = query,
        timestamp = timestamp
    )
}

fun ArtistCacheEntity.toDto(): ArtistCacheDto {
    return ArtistCacheDto(
        id = this.id,
        photoPath = this.photoPath,
        name = this.name,
    )
}