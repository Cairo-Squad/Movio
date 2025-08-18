package com.cairosquad.local.cache.movie

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.cairosquad.repository.movie.data_source.local.dto.CacheCodeMovieCrossRef
import com.cairosquad.repository.movie.data_source.local.dto.CacheCodeWithMoviesCacheDto
import com.cairosquad.repository.movie.data_source.local.dto.MovieGenreCacheCrossRef
import com.cairosquad.repository.movie.data_source.local.dto.MovieWithoutGenreCacheDto

@Dao
interface MoviesCacheDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCrossRefForCacheCodeAndMovieCache(crossRef: List<CacheCodeMovieCrossRef>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCrossRefForMovieAndGenreCache(crossRef: List<MovieGenreCacheCrossRef>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMoviesWithoutGenre(movies: List<MovieWithoutGenreCacheDto>)

    @Query("Delete from MovieWithoutGenreCacheDto where cachingTimestamp < :expirationTime")
    suspend fun deleteExpiredMovieWithoutGenreCache(expirationTime: Long)

    @Query("Delete from CacheCodeMovieCrossRef " +
            "where " +
                "Not movie_id_language in (Select movie_id from MovieWithoutGenreCacheDto) " +
             "OR " +
                "Not cacheCode in (Select cacheCode from CacheCodeDto)")
    suspend fun deleteCrossRefForNonExistingCacheCodeAndMovieCache()

    @Query("Delete from MovieGenreCacheCrossRef " +
            "where " +
                "Not movie_id_language in (Select movie_id from MovieWithoutGenreCacheDto) " +
             "OR " +
                "Not genre_id_language in (Select genre_id_language from MovieGenreCacheCrossRef)")
    suspend fun deleteCrossRefForNonExistingMovieAndGenreCache()

    @Transaction
    @Query("Select * From CacheCodeDto where cacheCode = :cacheCode")
    suspend fun getMoviesByCacheCode(cacheCode: String): CacheCodeWithMoviesCacheDto?
}