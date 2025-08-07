package com.cairosquad.viewmodel.auth_gate

import com.cairosquad.domain.usecase.LoginUseCase
import com.cairosquad.domain.usecase.OnboardingUseCase
import javax.inject.Inject

class AuthGate @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val onboardingUseCase: OnboardingUseCase
) {
    suspend fun isUserLoggedIn(): Boolean {
        return try {
            loginUseCase.isUserLoggedIn()
        } catch (_: Exception) {
            false
        }
    }

    suspend fun isOnboardingStateComplete(): Boolean {
        return try {
            onboardingUseCase.getOnboardingState()
        } catch (_: Exception) {
            false
        }
    }
}