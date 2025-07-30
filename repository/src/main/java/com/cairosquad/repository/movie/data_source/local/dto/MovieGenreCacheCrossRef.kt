package com.cairosquad.repository.movie.data_source.local.dto

import androidx.room.ColumnInfo
import androidx.room.Entity


@Entity(
    tableName = "MovieGenreCacheCrossRef",
    primaryKeys = ["movie_id", "genre_id"]
)
data class MovieGenreCacheCrossRef(
    @ColumnInfo(name = "movie_id")
    val movieId: Long,
    @ColumnInfo(name = "genre_id")
    val genreId: Long
){
    companion object{
        fun fromMovie(
            movie: MovieCacheDto
        ): List<MovieGenreCacheCrossRef> {
            return movie.genres.map { genre ->
                MovieGenreCacheCrossRef(
                    movie.movieWithoutGenre.id,
                    genre.id
                )
            }
        }
    }
}