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
import com.cairosquad.design_system.theme.MovioTheme
import com.cairosquad.design_system.theme.Theme
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
            .navigationBarsPadding(),
        verticalArrangement = Arrangement.Bottom
    ) {
        Spacer(modifier = Modifier.weight(0.71f))
        Image(
            painter = painterResource(R.drawable.im_onboarding),
            contentDescription = null,
            contentScale = ContentScale.FillHeight,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f)
                .padding(bottom = 40.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Text(
                text = stringResource(R.string.open_with_space_end),
                style = Theme.textStyle.display.mediumMedium20,
                color = Theme.color.surfaces.onSurface,
                modifier = Modifier.padding(end = 6.dp)
            )

            Text(
                text = stringResource(R.string.movio),
                style = Theme.textStyle.display.largeBold24.copy(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Theme.color.brand.onPrimary,
                            Theme.color.brand.primary
                        )
                    )
                ),
            )
        }

        Text(
            text = stringResource(R.string.and_let_the_noise_fade_away),
            style = Theme.textStyle.display.mediumMedium20,
            color = Theme.color.surfaces.onSurface,
            modifier = Modifier
                .padding(bottom = 12.dp)
                .padding(horizontal = 16.dp)
        )

        Text(
            text = stringResource(R.string.swipe_pick_dive_in_and_be_the_star_of_the_scene),
            style = Theme.textStyle.label.smallRegular12,
            color = Theme.color.surfaces.onSurfaceContainer,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.weight(0.29f))

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