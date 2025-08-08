package com.cairosquad.local.onboarding

import android.content.Context
import com.cairosquad.local.data_store.DataStoreConstants
import com.cairosquad.local.data_store.MovioDataStore
import com.cairosquad.repository.onboarding.data_source.local.OnboardingDataSource
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class OnboardingDataSourceImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : OnboardingDataSource {
    override suspend fun setOnboardingStateAsCompleted() {
        MovioDataStore.setPrefValue(
            context = context,
            key = DataStoreConstants.ONBOARDING_STATE,
            value = true
        )
    }

    override suspend fun getOnboardingState(): Boolean {
        return MovioDataStore.getPrefValue(
            context = context,
            key = DataStoreConstants.ONBOARDING_STATE,
            defaultValue = false
        ) as Boolean
    }
}