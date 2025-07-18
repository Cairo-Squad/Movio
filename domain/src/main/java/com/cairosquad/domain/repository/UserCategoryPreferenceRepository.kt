package com.cairosquad.domain.repository

import com.cairosquad.entity.Genre
import com.cairosquad.entity.UserCategoryPreference

interface UserCategoryPreferenceRepository {
    suspend fun updatePreferences(genres: List<Genre>)
    suspend fun getTopPreferences(): Genre?
    suspend fun cleanOldPreferences()
}
