package com.cairosquad.repository.movie.data_source.local.dto

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation


data class MovieCacheDto(
    @Embedded
    val movieWithoutGenre: MovieWithoutGenreCacheDto,
    @Relation(
        parentColumn = "movie_id",
        entityColumn = "genre_id",
        associateBy = Junction(MovieGenreCacheCrossRef::class)
    )
    val genres: List<MovieGenreCacheDto>
)