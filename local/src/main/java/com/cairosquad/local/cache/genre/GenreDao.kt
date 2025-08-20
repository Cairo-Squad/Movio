package com.cairosquad.local.cache.genre

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cairosquad.repository.movie.data_source.local.dto.GenreOfMovieCacheDto
import com.cairosquad.repository.series.data_source.local.dto.GenreOfSeriesCacheDto

@Dao
interface GenreDao {
     @Insert(onConflict = OnConflictStrategy.REPLACE)
     suspend fun insertMovieGenres(genres: List<GenreOfMovieCacheDto>)

     @Insert(onConflict = OnConflictStrategy.REPLACE)
     suspend fun insertSeriesGenres(genres: List<GenreOfSeriesCacheDto>)

     @Query("Delete from GenreOfMovieCacheDto where cachingTimestamp < :expirationTime")
     suspend fun deleteExpiredGenreOfMovieCache(expirationTime: Long)

     @Query("Delete from GenreOfSeriesCacheDto where cachingTimestamp < :expirationTime")
     suspend fun deleteExpiredGenreOfSeriesCache(expirationTime: Long)

     @Query("Select * From GenreOfMovieCacheDto")
     suspend fun getMovieGenres(): List<GenreOfMovieCacheDto>

     @Query("Select * From GenreOfMovieCacheDto where language = :language")
     suspend fun getMovieGenresByLanguage(language: String): List<GenreOfMovieCacheDto>

     @Query("Select * From GenreOfMovieCacheDto")
     suspend fun getSeriesGenres(): List<GenreOfSeriesCacheDto>

     @Query("Select * From GenreOfMovieCacheDto where language = :language")
     suspend fun getSeriesGenresByLanguage(language: String): List<GenreOfSeriesCacheDto>

}