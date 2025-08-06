package com.cairosquad.ui.onboarding

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cairosquad.design_system.theme.MovioTheme
import com.cairosquad.ui.utils.ObserveAsEffect
import com.cairosquad.viewmodel.onboarding.OnboardingEffect
import com.cairosquad.viewmodel.onboarding.OnboardingInteractionListener
import com.cairosquad.viewmodel.onboarding.OnboardingViewModel

@Composable
fun OnboardingScreen(
    navigateToAuthOrHome: () -> Unit,
    onboardingViewModel: OnboardingViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    ObserveAsEffect(onboardingViewModel.effect) { effect ->
        when (effect) {
            OnboardingEffect.NavigateToAuthOrHome -> navigateToAuthOrHome()
        }
    }

    OnboardingContent(
        onboardingInteractionListener = onboardingViewModel,
        modifier = modifier
    )
}

@Composable
private fun OnboardingContent(
    onboardingInteractionListener: OnboardingInteractionListener,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(top = 105.dp),

        ) {

    }
}

@Preview
@Composable
private fun PreviewOnboardingScreen() {
    MovioTheme {
        OnboardingScreen(
            navigateToAuthOrHome = {}
        )
    }
}