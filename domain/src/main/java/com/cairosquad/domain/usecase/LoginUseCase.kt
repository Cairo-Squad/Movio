package com.cairosquad.domain.usecase

import com.cairosquad.domain.repository.LoginRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val loginRepository: LoginRepository
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