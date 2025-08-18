package com.cairosquad.domain.repository

import com.cairosquad.entity.Language
import kotlinx.coroutines.flow.Flow

interface LanguageRepository {
    fun getLanguageFlow(): Flow<Language>
    suspend fun getLanguage(): String
    suspend fun saveLanguage(language: Language)
}