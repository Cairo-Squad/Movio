package com.cairosquad.ui.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.cairosquad.design_system.basic_component.AppBar
import com.cairosquad.design_system.basic_component.WebView
import com.cairosquad.ui.navigation.LocalNavController


@Composable
fun ForgetPasswordWebViewScreen(url: String) {
    val navController = LocalNavController.current

    Column(
        modifier = Modifier
            .systemBarsPadding()
            .fillMaxSize()
    ) {

        AppBar(
            modifier = Modifier.fillMaxWidth(),
            title = "Reset Password",
            onBackButtonClicked = { navController.popBackStack() },
        )
        WebView(
            webPageUrl = url,
            Modifier.fillMaxSize(),
            onBackPressed = { navController.popBackStack() }
        )
    }
}