package com.cairosquad.repository.onboarding

import com.cairosquad.domain.repository.OnboardingRepository
import com.cairosquad.repository.onboarding.data_source.local.OnboardingDataSource

class OnboardingRepositoryImpl(
    private val dataSource: OnboardingDataSource
) : OnboardingRepository {
    override suspend fun setOnboardingStateAsCompleted() {
        dataSource.setOnboardingStateAsCompleted()
    }

    override suspend fun getOnboardingState(): Boolean {
        return dataSource.getOnboardingState()
    }
}