package com.cairosquad.domain.usecase

import com.cairosquad.domain.repository.LanguageRepository
import com.cairosquad.entity.Language
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LanguageManagerUseCase @Inject constructor(
    private val repository: LanguageRepository
) {

    fun getLanguage(): Flow<Language> {
        return repository.getLanguage()
    }

    suspend fun saveLanguage(language: Language) {
        repository.saveLanguage(language)
    }
}