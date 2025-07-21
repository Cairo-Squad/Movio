package com.cairosquad.ui.login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.basic_component.Button
import com.cairosquad.design_system.basic_component.Icon
import com.cairosquad.design_system.basic_component.InputField
import com.cairosquad.design_system.basic_component.Text
import com.cairosquad.design_system.theme.MovioTheme
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.ui.R
import com.cairosquad.ui.movio_component.LoginScreenHeader
import com.cairosquad.ui.navigation.ForgetPasswordWebViewRoute
import com.cairosquad.ui.navigation.LocalNavController
import com.cairosquad.ui.navigation.SignUpWebViewRoute
import com.cairosquad.ui.utils.ObserveAsEffect
import com.cairosquad.viewmodel.login.LoginEffect
import com.cairosquad.viewmodel.login.LoginInteractionListener
import com.cairosquad.viewmodel.login.LoginScreenState
import com.cairosquad.viewmodel.login.LoginViewModel
import org.koin.androidx.compose.koinViewModel


@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = koinViewModel()
) {
    val navController = LocalNavController.current
    val uiState by viewModel.screenState.collectAsState()
    val urlTest = "https://www.google.com"
    ObserveAsEffect(viewModel.effect) { effect ->
        when (effect) {
            LoginEffect.NavigateToForgetPassword -> navController.navigate(
                ForgetPasswordWebViewRoute(url = urlTest)
            )

            LoginEffect.NavigateToHome -> TODO()
            LoginEffect.NavigateToGuestHome -> TODO()
            LoginEffect.NavigateToSignUp -> navController.navigate(
                SignUpWebViewRoute(url = urlTest)
            )
        }
    }

    LoginScreenContent(
        uiState = uiState,
        interactionListener = viewModel,
        modifier = modifier
    )
}

@Composable
private fun LoginScreenContent(
    uiState: LoginScreenState,
    interactionListener: LoginInteractionListener,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .systemBarsPadding()
            .fillMaxSize()
            .background(color = Theme.color.surfaces.surface)
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {
        LoginScreenHeader(
            Modifier
                .fillMaxWidth()
                .padding(top = 74.dp, bottom = 48.dp)
        )

        InputField(
            value = uiState.username,
            onValueChange = { interactionListener.onUsernameChange(it) },
            placeholder = stringResource(R.string.user_name),
            isPasswordField = false,
            leadingIcon = R.drawable.profile_login,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        InputField(
            value = uiState.password,
            onValueChange = { interactionListener.onPasswordChange(it) },
            placeholder = stringResource(R.string.password),
            isPasswordField = true,
            leadingIcon = R.drawable.lock,
            trailingIcon = if (uiState.isPasswordVisible) R.drawable.eye else R.drawable.close_eye,
            onTrailingIconClick = { interactionListener.onPasswordVisibilityIconClick() },
            modifier = Modifier.padding(bottom = 12.dp)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            this@Column.AnimatedVisibility(
                visible = uiState.isPasswordIncorrect,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp)

                ) {
                    Icon(
                        painter = painterResource(R.drawable.info_circle),
                        contentDescription = stringResource(R.string.icon),
                        tint = Theme.color.system.errorContainer,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = stringResource(R.string.password_you_entered_is_incorrect),
                        style = Theme.textStyle.label.smallRegular12,
                        color = Theme.color.system.errorContainer,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = stringResource(R.string.forgot_password),
                modifier = Modifier
                    .clickable { interactionListener.onForgetPasswordClick() }
                    .padding(bottom = 24.dp),
                style = Theme.textStyle.label.mediumMedium12,
                color = Theme.color.surfaces.onSurfaceVariant,
            )
        }

        Button(
            text = stringResource(R.string.login),
            onClick = { interactionListener.onLoginClick() },
            modifier = Modifier.padding(bottom = 40.dp),
            isLoading = uiState.isLoading
        )

        Row(
            modifier = Modifier.padding(bottom = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(1.dp)
                    .background(color = Theme.color.surfaces.onSurfaceAt3)
            )
            Text(
                text = stringResource(R.string.or),
                color = Theme.color.surfaces.onSurfaceVariant,
                style = Theme.textStyle.label.smallRegular12
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(1.dp)
                    .background(color = Theme.color.surfaces.onSurfaceAt3)
            )
        }
        Button(
            text = stringResource(R.string.continue_as_a_guest),
            onClick = { interactionListener.onContinueAsAGuestClick() },
            modifier = Modifier,
            textStyle = Theme.textStyle.label.mediumMedium14,
            textColor = Theme.color.surfaces.onSurface,
            containerColor = Theme.color.surfaces.surface,
            borderColor = Theme.color.surfaces.onSurfaceAt3
        )
        Row(
            modifier = Modifier
                .wrapContentWidth()
                .weight(1f)
                .padding(bottom = 32.dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = stringResource(R.string.dont_have_an_account),
                color = Theme.color.surfaces.onSurfaceVariant,
                style = Theme.textStyle.label.smallRegular14
            )
            Text(
                text = stringResource(R.string.signup),
                color = Theme.color.brand.primary,
                style = Theme.textStyle.label.mediumMedium14,
                modifier = Modifier.clickable { interactionListener.onSignUpClick() }
            )
        }

    }
}

@Preview
@Composable
private fun PreviewLoginScreen() {
    MovioTheme {
        LoginScreenContent(
            uiState = LoginScreenState(),
            interactionListener = PreviewLoginInteractionListener
        )
    }
}

object PreviewLoginInteractionListener : LoginInteractionListener {
    override fun onUsernameChange(username: String) {
        TODO("Not yet implemented")
    }

    override fun onPasswordChange(password: String) {
        TODO("Not yet implemented")
    }

    override fun onPasswordVisibilityIconClick() {
        TODO("Not yet implemented")
    }

    override fun onForgetPasswordClick() {
        TODO("Not yet implemented")
    }

    override fun onLoginClick() {
        TODO("Not yet implemented")
    }

    override fun onContinueAsAGuestClick() {
        TODO("Not yet implemented")
    }

    override fun onSignUpClick() {
        TODO("Not yet implemented")
    }
}