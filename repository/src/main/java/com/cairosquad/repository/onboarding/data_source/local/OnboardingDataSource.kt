package com.cairosquad.repository.onboarding.data_source.local

interface OnboardingDataSource {
    suspend fun setOnboardingStateAsCompleted()
    suspend fun getOnboardingState(): Boolean
}