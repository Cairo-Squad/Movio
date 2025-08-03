package com.cairosquad.local.data_store

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.lang.ref.WeakReference

object MovioDataStore {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "MovioDataStore")

    suspend fun setPrefValue(
        context: Context,
        key: String,
        value: Any
    ) {
        val contextWeakReference = WeakReference(context)
        contextWeakReference.get()?.let { cont ->
            cont.dataStore.edit { preferences ->

                when (value) {
                    is Int -> preferences[intPreferencesKey(key)] = value
                    is String -> preferences[stringPreferencesKey(key)] = value
                    is Boolean -> preferences[booleanPreferencesKey(key)] = value
                    is Long -> preferences[longPreferencesKey(key)] = value
                    is Float -> preferences[floatPreferencesKey(key)] = value
                    is Double -> preferences[doublePreferencesKey(key)] = value
                }
            }
        }
    }

    suspend fun getPrefValue(context: Context?, key: String, defaultValue: Any): Any {
        val contextWeakReference = WeakReference(context)
        return contextWeakReference.get()?.let { ctx ->
            val dataStoreValue: Flow<Any> = ctx.dataStore.data.map { preferences ->
                when (defaultValue) {
                    is String -> preferences[stringPreferencesKey(key)] ?: defaultValue
                    is Int -> preferences[intPreferencesKey(key)] ?: defaultValue
                    is Boolean -> preferences[booleanPreferencesKey(key)] ?: defaultValue
                    is Long -> preferences[longPreferencesKey(key)] ?: defaultValue
                    is Float -> preferences[floatPreferencesKey(key)] ?: defaultValue
                    is Double -> preferences[doublePreferencesKey(key)] ?: defaultValue

                    else -> defaultValue
                }
            }
            dataStoreValue.first()
        } ?: defaultValue
    }
}