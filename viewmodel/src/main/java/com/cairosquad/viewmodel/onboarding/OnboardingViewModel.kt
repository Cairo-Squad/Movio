package com.cairosquad.viewmodel.onboarding

import com.cairosquad.domain.usecase.OnboardingUseCase
import com.cairosquad.viewmodel.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val onboardingUseCase: OnboardingUseCase
) : BaseViewModel<Unit, OnboardingEffect>(Unit), OnboardingInteractionListener {
    init {
        tryToCall(
            block = { onboardingUseCase.getOnboardingState() },
            onSuccess = { isCompleted ->
                if (isCompleted) {
                    sendEffect(OnboardingEffect.NavigateToAuth)
                }
            },
            onError = { }
        )
    }

    override fun onCompleteOnboarding() {
        tryToCall(
            block = { onboardingUseCase.setOnboardingStateAsCompleted() },
            onSuccess = { sendEffect(OnboardingEffect.NavigateToAuth) },
            onError = { sendEffect(OnboardingEffect.NavigateToAuth) }
        )
    }

}