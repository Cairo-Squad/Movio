package com.cairosquad.domain.repository

import com.cairosquad.entity.Artist

interface SearchRepository {
    suspend fun getArtists(query: String,page:Int): List<Artist>

    suspend fun getAllHistory(): List<String>

    suspend fun getAllHistoryByQuery(query: String): List<String>

    suspend fun clearAll()

    suspend fun removeQuery(query: String)

    suspend fun addQuery(query: String)
}
