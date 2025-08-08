package com.cairosquad.repository.theme

import kotlinx.coroutines.flow.Flow

interface ThemeDataSource {
    suspend fun saveTheme(theme: String)
    fun getTheme(): Flow<String>
}
