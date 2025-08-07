package com.cairosquad.local.theme

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.cairosquad.repository.theme.ThemeDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

val Context.themeDataStore: DataStore<Preferences> by preferencesDataStore(name = "theme_prefs")

class ThemeDataStoreSourceImpl @Inject constructor(
    private val context: Context
) : ThemeDataSource {
    private val dataStore = context.themeDataStore

    companion object {
        val THEME_KEY = stringPreferencesKey("app_theme")
    }

    override suspend fun saveTheme(theme: String) {
        dataStore.edit { preferences ->
            preferences[THEME_KEY] = theme
        }
    }

    override fun getTheme(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[THEME_KEY] ?: "dark"
        }
    }
}
