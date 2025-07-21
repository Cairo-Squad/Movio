package com.cairosquad.ui.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.R
import androidx.compose.ui.res.stringResource
import com.cairosquad.design_system.basic_component.AppBar
import com.cairosquad.design_system.basic_component.WebView
import com.cairosquad.ui.navigation.LocalNavController


@Composable
fun ForgetPasswordWebViewScreen(url: String) {
    val navController = LocalNavController.current

    Column(
        modifier = Modifier.systemBarsPadding()
    ) {
        AppBar(
            modifier = Modifier.fillMaxWidth(),
            title = "Reset Password",
            onBackButtonClicked = { navController.popBackStack() },
        )
        WebView(url, Modifier.fillMaxSize())
    }
}