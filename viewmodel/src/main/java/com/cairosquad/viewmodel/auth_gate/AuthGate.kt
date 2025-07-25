package com.cairosquad.viewmodel.auth_gate

import com.cairosquad.domain.usecase.authentication.LoginUseCase
import kotlinx.coroutines.runBlocking

class AuthGate(
    private val loginUseCase: LoginUseCase
) {
    fun isUserLoggedIn(): Boolean {
        return runBlocking {
            try {
                loginUseCase.isUserLoggedIn()
            } catch (_: Exception) {
                false
            }
        }
    }
}