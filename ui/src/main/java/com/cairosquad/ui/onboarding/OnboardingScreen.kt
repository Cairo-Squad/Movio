package com.cairosquad.ui.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cairosquad.design_system.basic_component.Text
import com.cairosquad.design_system.modifier.dropShadow
import com.cairosquad.design_system.theme.MovioTheme
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.ui.R
import com.cairosquad.ui.movio_component.SwitchToStartButton
import com.cairosquad.ui.onboarding.content.OnboardingContent
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