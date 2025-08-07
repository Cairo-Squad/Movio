package com.cairosquad.viewmodel.onboarding

import com.cairosquad.domain.usecase.OnboardingUseCase
import com.cairosquad.viewmodel.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val onboardingUseCase: OnboardingUseCase
) : BaseViewModel<DummyState, OnboardingEffect>(DummyState()), OnboardingInteractionListener {
    override fun onCompleteOnboarding() {
        tryToCall(
            block = { onboardingUseCase.setOnboardingStateAsCompleted() },
            onSuccess = { sendEffect(OnboardingEffect.NavigateToAuthOrHome) },
            onError = { sendEffect(OnboardingEffect.NavigateToAuthOrHome) }
        )
    }
}