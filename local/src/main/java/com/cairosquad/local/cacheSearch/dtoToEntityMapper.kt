package com.cairosquad.local.cacheSearch

import com.cairosquad.local.cacheSearch.entity.ArtistCacheEntity
import com.cairosquad.local.cacheSearch.entity.MovieCacheEntity
import com.cairosquad.local.cacheSearch.entity.SeriesCacheEntity
import com.cairosquad.repository.dataSource.local.Dto.ArtistCacheDto
import com.cairosquad.repository.dataSource.local.Dto.MovieCacheDto
import com.cairosquad.repository.dataSource.local.Dto.SeriesCacheDto

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

fun List<MovieCacheDto>.toEntity(
    query: String,
    timestamp: Long
): List<MovieCacheEntity> {
    return this.map { movieCacheDto ->
        movieCacheDto.toEntity(query, timestamp)
    }
}

fun MovieCacheEntity.toDto(): MovieCacheDto {
    return MovieCacheDto(
        id = this.id,
        posterPath = this.posterPath,
        title = this.title,
        voteAverage = this.voteAverage,
    )
}

fun List<MovieCacheEntity>.toDto(): List<MovieCacheDto> {
    return this.map { movieCacheEntity ->
        movieCacheEntity.toDto()
    }
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

fun List<SeriesCacheDto>.toEntity(
    query: String,
    timestamp: Long
): List<SeriesCacheEntity> {
    return this.map { movieCacheDto ->
        movieCacheDto.toEntity(query, timestamp)
    }
}

fun SeriesCacheEntity.toDto(): SeriesCacheDto {
    return SeriesCacheDto(
        id = this.id,
        posterPath = this.posterPath,
        name = this.name,
        voteAverage = this.voteAverage,
    )
}

fun List<SeriesCacheEntity>.toDto(): List<SeriesCacheDto> {
    return this.map { movieCacheEntity ->
        movieCacheEntity.toDto()
    }
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

fun List<ArtistCacheDto>.toEntity(
    query: String,
    timestamp: Long
): List<ArtistCacheEntity> {
    return this.map { movieCacheDto ->
        movieCacheDto.toEntity(query, timestamp)
    }
}

fun ArtistCacheEntity.toDto(): ArtistCacheDto {
    return ArtistCacheDto(
        id = this.id,
        photoPath = this.photoPath,
        name = this.name,
    )
}

fun List<ArtistCacheEntity>.toDto(): List<ArtistCacheDto> {
    return this.map { movieCacheEntity ->
        movieCacheEntity.toDto()
    }
}