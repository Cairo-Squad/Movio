package com.cairosquad.repository.language

import com.cairosquad.domain.repository.LanguageRepository
import com.cairosquad.entity.Language
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LanguageRepositoryImpl @Inject constructor(
    private val languageDataSource: LanguageDataSource
) : LanguageRepository {

    override suspend fun saveLanguage(language: Language) {
        languageDataSource.saveLanguage(language.code)
    }

    override fun getLanguage(): Flow<Language> {
        return languageDataSource.getLanguage().map { languageCode ->
            when (languageCode) {
                "ar" -> Language("ar", "العربية")
                else -> Language("en", "English")
            }
        }
    }
}