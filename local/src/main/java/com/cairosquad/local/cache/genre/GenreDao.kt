package com.cairosquad.local.cache.genre

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cairosquad.repository.movie.data_source.local.dto.GenreOfMovieCacheDto

@Dao
interface GenreDao {
     @Insert(onConflict = OnConflictStrategy.REPLACE)
     suspend fun insertMovieGenres(genres: List<GenreOfMovieCacheDto>)

     @Query("Delete from GenreOfMovieCacheDto where timestamp < :expirationTime")
     suspend fun deleteExpiredMovieGenreCache(expirationTime: Long)

     @Query("Select * From GenreOfMovieCacheDto")
     suspend fun getMovieGenres(): List<GenreOfMovieCacheDto>

}