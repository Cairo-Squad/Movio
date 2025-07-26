package com.cairosquad.repository.movie.data_source.local.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.cairosquad.repository.utils.sharedDto.local.RequestCacheDto


@Entity(
    tableName = "RequestMovieCacheCrossRef",
    primaryKeys = ["request", "movie_id"]
)
data class RequestMovieCacheCrossRef(
    @ColumnInfo(name = "request")
    val request: String,
    @ColumnInfo(name = "movie_id")
    val movieId: Long,
) {
    companion object{
        fun fromRequestAndMovieList(
            request: RequestCacheDto,
            movies: List<MovieCacheDto>
        ): List<RequestMovieCacheCrossRef> {
            return movies.map { movie ->
                RequestMovieCacheCrossRef(
                    request.request,
                    movie.movieWithoutGenre.id
                )
            }
        }
    }
}
