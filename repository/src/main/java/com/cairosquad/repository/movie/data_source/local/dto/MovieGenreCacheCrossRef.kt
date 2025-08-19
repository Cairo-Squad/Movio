package com.cairosquad.repository.movie.data_source.local.dto

import androidx.room.ColumnInfo
import androidx.room.Entity


@Entity(
    tableName = "MovieGenreCacheCrossRef",
    primaryKeys = ["movie_id_language", "genre_id_language"]
)
data class MovieGenreCacheCrossRef(
    @ColumnInfo(name = "movie_id_language")
    val movieIdWithLanguage: String,
    @ColumnInfo(name = "genre_id_language")
    val genreIdWithLanguage: String
){
    companion object{
        fun fromMovie(
            movie: MovieCacheDto
        ): List<MovieGenreCacheCrossRef> {
            return movie.genres.map { genre ->
                MovieGenreCacheCrossRef(
                    movie.movieWithoutGenre.movieIdWithLanguage,
                    genre.genreIdWithLanguage
                )
            }
        }
    }
}