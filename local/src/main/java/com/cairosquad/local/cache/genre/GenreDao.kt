package com.cairosquad.local.cache.genre

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cairosquad.repository.movie.data_source.local.dto.MovieGenreCacheDto

@Dao
interface GenreDao {
     @Insert(onConflict = OnConflictStrategy.REPLACE)
     suspend fun insertMovieGenres(genres: List<MovieGenreCacheDto>)

     @Query("Delete from MovieGenreCacheDto where timestamp < :expirationTime")
     suspend fun deleteExpiredMovieGenreCache(expirationTime: Long)

     @Query("Select * From MovieGenreCacheDto")
     suspend fun getMovieGenres(): List<MovieGenreCacheDto>

}