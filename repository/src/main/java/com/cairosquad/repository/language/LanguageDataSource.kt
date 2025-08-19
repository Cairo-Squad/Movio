package com.cairosquad.repository.language

import kotlinx.coroutines.flow.Flow

interface LanguageDataSource {
    suspend fun saveLanguage(languageCode: String)
    fun getLanguageFlow(): Flow<String>
    suspend fun getLanguage(): String
}