package com.cairosquad.local.search.recommendation.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.cairosquad.repository.search.data_source.local.dto.GenreDto
import com.cairosquad.repository.search.data_source.local.dto.UserCategoryPreferenceDto

@Dao
interface UserCategoryPreferenceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(preference: List<GenreDto>)

    @Update
    suspend fun updateGenres(genres: List<GenreDto>)

    @Query("SELECT * FROM genres ORDER BY click_Count DESC LIMIT 1")
    suspend fun getTopCategories(): GenreDto?

    @Query("SELECT * FROM genres WHERE genre_name IN (:names)")
    suspend fun getGenresByNames(names: List<String>): List<GenreDto>
}