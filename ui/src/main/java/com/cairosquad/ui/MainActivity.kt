package com.cairosquad.ui

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.cairosquad.ui.localeWrapper.LocaleWrapper
import com.cairosquad.ui.navigation.AppNavigation
import com.cairosquad.viewmodel.auth_gate.AuthGate
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale
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
            LocaleWrapper(::setAppDisplayLanguage) {
                AppNavigation(authGate)
            }
        }
    }

    private fun setAppDisplayLanguage(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val configuration = Configuration(resources.configuration)
        configuration.setLocale(locale)
        if (languageCode == "ar") {
            configuration.setLayoutDirection(Locale("ar"))
            window.decorView.layoutDirection = View.LAYOUT_DIRECTION_RTL
        } else {
            configuration.setLayoutDirection(Locale("en"))
            window.decorView.layoutDirection = View.LAYOUT_DIRECTION_LTR
        }
        @Suppress("DEPRECATION")
        resources.updateConfiguration(configuration, resources.displayMetrics)
        @Suppress("DEPRECATION")
        applicationContext.resources.updateConfiguration(
            configuration,
            applicationContext.resources.displayMetrics
        )
    }
}