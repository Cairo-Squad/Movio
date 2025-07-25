package com.cairosquad.local.cache.genre

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cairosquad.repository.movie.data_source.local.dto.GenreCacheDtoNew

@Dao
interface GenreDao {
     @Insert(onConflict = OnConflictStrategy.REPLACE)
     suspend fun insertGenres(genres: List<GenreCacheDtoNew>)

     @Query("Delete from GenreCacheDtoNew where timestamp < :expirationTime")
     suspend fun deleteExpiredGenreCache(expirationTime: Long)

}