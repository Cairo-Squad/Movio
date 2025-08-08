package com.cairosquad.domain.repository

import com.cairosquad.entity.Theme
import kotlinx.coroutines.flow.Flow

interface ThemeRepository {
    fun getTheme(): Flow<Theme>
    suspend fun saveTheme(theme: Theme)
}