package com.cairosquad.repository.movie.data_source.local.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.cairosquad.repository.utils.sharedDto.local.CacheCodeDto


@Entity(
    tableName = "CacheCodeMovieCrossRef",
    primaryKeys = ["cacheCode", "movie_id"]
)
data class CacheCodeMovieCrossRef(
    @ColumnInfo(name = "cacheCode")
    val cacheCode: String,
    @ColumnInfo(name = "movie_id")
    val movieId: Long,
) {
    companion object{
        fun fromCacheCodeAndMovieList(
            cacheCode: CacheCodeDto,
            movies: List<MovieCacheDto>
        ): List<CacheCodeMovieCrossRef> {
            return movies.map { movie ->
                CacheCodeMovieCrossRef(
                    cacheCode.cacheCode,
                    movie.movieWithoutGenre.id
                )
            }
        }
    }
}
