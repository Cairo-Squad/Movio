package com.cairosquad.domain.usecase.authentication

import com.cairosquad.domain.repository.LoginRepository

class LoginUseCase(
    private val loginRepository: LoginRepository,
) {

    suspend fun login(username: String, password: String) {
        loginRepository.login(username, password)
    }

    suspend fun isUserLoggedIn(): Boolean {
        return loginRepository.isUserLoggedIn()
    }

    suspend fun logout() {
        loginRepository.logout()
    }
}