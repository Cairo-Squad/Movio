package com.cairosquad.domain.repository

interface LoginRepository {
    suspend fun login(username: String, password: String)

    suspend fun isUserLoggedIn(): Boolean

    suspend fun logout()
}