package com.cairosquad.viewmodel.auth_gate

import com.cairosquad.domain.usecase.LoginUseCase
import javax.inject.Inject

class AuthGate @Inject constructor(
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