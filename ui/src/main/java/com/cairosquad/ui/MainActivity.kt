package com.cairosquad.ui

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.cairosquad.design_system.theme.MovioTheme
import com.cairosquad.ui.navigation.AppNavigation
import com.cairosquad.viewmodel.auth_gate.AuthGate
import com.cairosquad.viewmodel.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var authGate: AuthGate
    private val mainViewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            mainViewModel.currentLanguage
                .collect { newLanguage ->
                    val currentLocale = resources.configuration.locales[0]

                    if (currentLocale.language != newLanguage.code) {
                        setAppLocale(newLanguage.code)
                        recreate()
                    }
                }
        }

        enableEdgeToEdge()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            window.setNavigationBarContrastEnforced(false)
        }

        setContent {
            val theme = mainViewModel.currentTheme.collectAsStateWithLifecycle()
            val isSystemInDarkTheme = when (theme.value) {
                com.cairosquad.viewmodel.main.Theme.LIGHT -> false
                com.cairosquad.viewmodel.main.Theme.DARK -> true
            }
            MovioTheme(
                isDarkTheme = isSystemInDarkTheme
            ) {
                AppNavigation(authGate)
            }
        }


    }
    private fun setAppLocale(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val configuration = Configuration(resources.configuration)
        configuration.setLocale(locale)
        @Suppress("DEPRECATION")
        resources.updateConfiguration(configuration, resources.displayMetrics)
    }
}