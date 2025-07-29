package com.cairosquad.ui

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.cairosquad.design_system.theme.MovioTheme
import com.cairosquad.ui.navigation.AppNavigation
import com.cairosquad.viewmodel.auth_gate.AuthGate
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var authGate: AuthGate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            window.setNavigationBarContrastEnforced(false)
        }

        setContent {
            MovioTheme {
                AppNavigation(authGate)
            }
        }
    }
}