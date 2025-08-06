package com.cairosquad.viewmodel.onboarding

sealed interface OnboardingEffect {
    data object NavigateToAuth : OnboardingEffect
}