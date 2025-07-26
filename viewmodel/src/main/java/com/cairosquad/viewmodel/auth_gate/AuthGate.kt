package com.cairosquad.viewmodel.auth_gate

import com.cairosquad.domain.usecase.authentication.LoginUseCase

class AuthGate(
    private val loginUseCase: LoginUseCase
) {
    suspend fun isUserLoggedIn(): Boolean {
        return try {
            loginUseCase.isUserLoggedIn()
        } catch (_: Exception) {
            false
        }
    }
}