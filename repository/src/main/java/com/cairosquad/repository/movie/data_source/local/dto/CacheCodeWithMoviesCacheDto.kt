package com.cairosquad.repository.movie.data_source.local.dto

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.cairosquad.repository.utils.sharedDto.local.CacheCodeDto

data class CacheCodeWithMoviesCacheDto(
    @Embedded
    val cacheCode: CacheCodeDto,
    @Relation(
        parentColumn = "cacheCode",
        entity = MovieWithoutGenreCacheDto::class,
        entityColumn = "movie_id_language",
        associateBy = Junction(CacheCodeMovieCrossRef::class)
    )
    val movies: List<MovieCacheDto>,
)
