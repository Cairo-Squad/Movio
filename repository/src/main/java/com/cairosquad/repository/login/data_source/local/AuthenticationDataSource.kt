package com.cairosquad.repository.login.data_source.local

interface AuthenticationDataSource {
    suspend fun saveSessionId(sessionId: String)

    suspend fun getSessionId(): String
}