package com.cairosquad.repository.movie.data_source.local.dto

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation


data class MovieCacheDto(
    @Embedded
    val movieWithoutGenre: MovieWithoutGenreCacheDto,
    @Relation(
        parentColumn = "movie_id_language",
        entityColumn = "genre_id_language",
        associateBy = Junction(MovieGenreCacheCrossRef::class)
    )
    val genres: List<GenreOfMovieCacheDto>
)