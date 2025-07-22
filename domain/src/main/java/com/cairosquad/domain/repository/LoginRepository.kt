package com.cairosquad.domain.repository

interface LoginRepository {
    suspend fun login(username: String, password: String)

    suspend fun logout()

}