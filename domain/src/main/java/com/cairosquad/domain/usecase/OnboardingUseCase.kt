package com.cairosquad.domain.usecase

import com.cairosquad.domain.repository.OnboardingRepository
import javax.inject.Inject

class OnboardingUseCase @Inject constructor(
    private val onboardingRepository: OnboardingRepository
) {
    suspend fun setOnboardingStateAsCompleted() {
        onboardingRepository.setOnboardingStateAsCompleted()
    }

    suspend fun getOnboardingState(): Boolean {
        return onboardingRepository.getOnboardingState()
    }
}