package com.cairosquad.ui.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cairosquad.design_system.theme.MovioTheme
import com.cairosquad.ui.R
import com.cairosquad.ui.movio_component.SwitchToStartButton
import com.cairosquad.ui.utils.ObserveAsEffect
import com.cairosquad.viewmodel.onboarding.OnboardingEffect
import com.cairosquad.viewmodel.onboarding.OnboardingInteractionListener
import com.cairosquad.viewmodel.onboarding.OnboardingViewModel

@Composable
fun OnboardingScreen(
    navigateToAuthOrHome: () -> Unit,
    modifier: Modifier = Modifier,
    onboardingViewModel: OnboardingViewModel = hiltViewModel(),
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
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(top = 105.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        Image(
            painter = painterResource(R.drawable.im_onboarding),
            contentDescription = null,
            contentScale = ContentScale.FillHeight,
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .padding(bottom = 40.dp)
        )

        SwitchToStartButton(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(bottom = 32.dp),
            onSwipeComplete = { onboardingInteractionListener.onCompleteOnboarding() }
        )
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