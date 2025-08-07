package com.cairosquad.local.language

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.cairosquad.repository.language.LanguageDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

val Context.languageDataStore: DataStore<Preferences> by preferencesDataStore(name = "language_prefs")

class LanguageDataStoreSourceImpl @Inject constructor(
    private val context: Context
) :
    LanguageDataSource {
    private val dataStore = context.languageDataStore

    companion object {
        val LANGUAGE_KEY = stringPreferencesKey("app_language")
    }

    override suspend fun saveLanguage(languageCode: String) {
        dataStore.edit { preferences ->
            preferences[LANGUAGE_KEY] = languageCode
        }
    }

    override fun getLanguage(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[LANGUAGE_KEY] ?: "en"
        }
    }
}
