package com.cairosquad.repository.search.data_source.local

import com.cairosquad.entity.Genre
import com.cairosquad.repository.search.data_source.local.dto.GenreDto

interface UserCategoryPreferenceLocalDataSource {
    suspend fun updatePreferences(genres: List<GenreDto>)
    suspend fun getTopPreferences(): GenreDto?
    suspend fun cleanOldPreferences()
}