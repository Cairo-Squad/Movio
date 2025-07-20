package com.cairosquad.ui.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cairosquad.design_system.theme.MovioTheme
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
    val uiState by viewModel.screenState.collectAsState()

    ObserveAsEffect(viewModel.effect) { effect ->
        when (effect) {
            LoginEffect.NavigateToForgetPassword -> TODO()
            LoginEffect.NavigateToHome -> TODO()
            LoginEffect.NavigateToGuestHome -> TODO()
            LoginEffect.NavigateToSignUp -> TODO()
        }
    }

    LoginScreenContent(
        uiState = uiState,
        interactionListener = viewModel,
        modifier = modifier
    )
}

@Composable
fun LoginScreenContent(
    uiState: LoginScreenState,
    interactionListener: LoginInteractionListener,
    modifier: Modifier = Modifier
) {
    // TODO: Login Screen Content
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