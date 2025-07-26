package com.cairosquad.repository.movie.data_source.local.dto

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.cairosquad.repository.utils.sharedDto.local.RequestCacheDto

data class RequestWithMoviesCacheDto(
    @Embedded
    val request: RequestCacheDto,
    @Relation(
        parentColumn = "request",
        entity = MovieWithoutGenreCacheDto::class,
        entityColumn = "movie_id",
        associateBy = Junction(RequestMovieCacheCrossRef::class)
    )
    val movies: List<MovieCacheDto>,
)
