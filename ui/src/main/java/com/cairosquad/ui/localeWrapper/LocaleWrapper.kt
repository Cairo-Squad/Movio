package com.cairosquad.ui.localeWrapper

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.cairosquad.design_system.theme.MovioTheme
import com.cairosquad.viewmodel.main.LocaleViewModel
import com.cairosquad.viewmodel.main.Theme

@Composable
fun LocaleWrapper(
    appDisplayLanguageSitter: (languageCode: String) -> Unit,
    localeViewModel: LocaleViewModel = hiltViewModel(),
    content: @Composable () -> Unit
) {

    LaunchedEffect(Unit) {
        localeViewModel.getLanguageCode().collect {
            appDisplayLanguageSitter(it)
        }
    }

    val theme by localeViewModel.currentTheme.collectAsState()

    MovioTheme(
        isDarkTheme = theme == Theme.DARK
    ) {
        content()
    }
}