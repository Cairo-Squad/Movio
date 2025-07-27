package com.cairosquad.local.cache.movie

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.cairosquad.repository.movie.data_source.local.dto.MovieGenreCacheCrossRef
import com.cairosquad.repository.movie.data_source.local.dto.MovieWithoutGenreCacheDto
import com.cairosquad.repository.movie.data_source.local.dto.RequestMovieCacheCrossRef
import com.cairosquad.repository.movie.data_source.local.dto.RequestWithMoviesCacheDto

@Dao
interface MoviesCacheDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCrossRefForRequestAndMovieCache(mappings: List<RequestMovieCacheCrossRef>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCrossRefForMovieAndGenreCache(mappings: List<MovieGenreCacheCrossRef>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMoviesWithoutGenre(movies: List<MovieWithoutGenreCacheDto>)

    @Query("Delete from MovieWithoutGenreCacheDto where timestamp < :expirationTime")
    suspend fun deleteExpiredMovieWithoutGenreCache(expirationTime: Long)

    @Query("Delete from RequestMovieCacheCrossRef " +
            "where " +
                "Not movie_id in (Select movie_id from MovieWithoutGenreCacheDto) " +
             "OR " +
                "Not request in (Select request from RequestCacheDto)")
    suspend fun deleteCrossRefForNonExistingRequestAndMovieCache()

    @Query("Delete from MovieGenreCacheCrossRef " +
            "where " +
                "Not movie_id in (Select movie_id from MovieWithoutGenreCacheDto) " +
             "OR " +
                "Not genre_id in (Select genre_id from MovieGenreCacheCrossRef)")
    suspend fun deleteCrossRefForNonExistingMovieAndGenreCache()

    @Transaction
    @Query("Select * From RequestCacheDto where request = :request")
    suspend fun getMoviesByRequest(request: String): RequestWithMoviesCacheDto?
}