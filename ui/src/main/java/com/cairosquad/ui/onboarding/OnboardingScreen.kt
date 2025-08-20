package com.cairosquad.ui.onboarding

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.cairosquad.ui.onboarding.content.OnboardingContent
import com.cairosquad.ui.utils.ObserveAsEffect
import com.cairosquad.viewmodel.onboarding.OnboardingEffect
import com.cairosquad.viewmodel.onboarding.OnboardingViewModel

@Composable
fun OnboardingScreen(
    navigateToLoginScreen: () -> Unit,
    modifier: Modifier = Modifier,
    onboardingViewModel: OnboardingViewModel = hiltViewModel(),
) {
    ObserveAsEffect(onboardingViewModel.effect) { effect ->
        when (effect) {
            OnboardingEffect.NavigateToAuthOrHome -> navigateToLoginScreen()
        }
    }

    OnboardingContent(
        onboardingInteractionListener = onboardingViewModel,
        modifier = modifier
    )
}