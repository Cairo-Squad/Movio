package com.cairosquad.repository.login.data_source.local

interface AuthenticationDataSource {
    fun saveSessionId(sessionId: String)

    fun getSessionId(): String
}