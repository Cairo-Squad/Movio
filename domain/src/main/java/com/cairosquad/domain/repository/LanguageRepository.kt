package com.cairosquad.domain.repository

import com.cairosquad.entity.Language
import kotlinx.coroutines.flow.Flow

interface LanguageRepository {
    fun getLanguage(): Flow<Language>
    suspend fun saveLanguage(language: Language)
}