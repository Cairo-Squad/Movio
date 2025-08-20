package com.cairosquad.ui.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.cairosquad.design_system.basic_component.SnackBar
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.ui.auth.content.LoginScreenContent
import com.cairosquad.ui.navigation.AppRoute
import com.cairosquad.ui.navigation.ForgetPasswordWebViewRoute
import com.cairosquad.ui.navigation.LocalNavController
import com.cairosquad.ui.navigation.LoginRoute
import com.cairosquad.ui.navigation.SignUpWebViewRoute
import com.cairosquad.ui.utils.ObserveAsEffect
import com.cairosquad.viewmodel.login.LoginEffect
import com.cairosquad.viewmodel.login.LoginViewModel


@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val navController = LocalNavController.current
    val uiState by viewModel.screenState.collectAsState()
    val resetPasswordUrl = "https://www.themoviedb.org/reset-password"
    val signUpUrl = "https://www.themoviedb.org/signup"
    var snackBarMessage by remember { mutableStateOf<String?>(null) }

    ObserveAsEffect(viewModel.effect) { effect ->
        when (effect) {
            LoginEffect.NavigateToForgetPassword -> navController.navigate(
                ForgetPasswordWebViewRoute(url = resetPasswordUrl)
            )

            LoginEffect.NavigateToHome -> {
                navController.navigate(AppRoute) {
                    popUpTo(LoginRoute) {
                        inclusive = true
                    }
                }
            }

            LoginEffect.NavigateToGuestHome -> {
                navController.navigate(AppRoute) {
                    popUpTo(LoginRoute) {
                        inclusive = true
                    }
                }
            }

            LoginEffect.NavigateToSignUp -> navController.navigate(
                SignUpWebViewRoute(url = signUpUrl)
            )
        }
    }

    LoginScreenContent(
        uiState = uiState,
        interactionListener = viewModel,
        modifier = modifier
    )

    AnimatedVisibility(visible = snackBarMessage != null) {
        SnackBar(
            message = snackBarMessage.orEmpty(),
            imageVector = ImageVector.vectorResource(
                id =
                    if (Theme.isDark) com.cairosquad.design_system.R.drawable.snack_bar_icon_fail_dark
                    else com.cairosquad.design_system.R.drawable.snack_bar_icon_fail_light
            ),
            action = {
                snackBarMessage =
                    stringResource(com.cairosquad.design_system.R.string.error_parsing_data)
            }
        )
    }
}