package com.cairosquad.domain.repository

interface OnboardingRepository {
    suspend fun setOnboardingStateAsCompleted()
    suspend fun getOnboardingState(): Boolean
}