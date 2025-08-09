package com.cairosquad.viewmodel.more

interface MoreScreenInteractionListener {
    fun onLoginClick()

    fun onMyRatingsClick()

    fun onThemeClick()
    fun onThemeBottomSheetDismiss()
    fun onThemeBottomSheetConfirm(theme: MoreScreenState.Theme)

    fun onLanguageClick()
    fun onLanguageBottomSheetConfirm(language: MoreScreenState.Language)
    fun onLanguageBottomSheetDismiss()

    fun onLogoutClick()
    fun onLogoutConfirm()
    fun onLogoutDismiss()
}