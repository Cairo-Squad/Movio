package com.cairosquad.viewmodel.more

import androidx.lifecycle.viewModelScope
import com.cairosquad.domain.usecase.AccountUseCase
import com.cairosquad.domain.usecase.GetAppVersionUseCase
import com.cairosquad.domain.usecase.LanguageManagerUseCase
import com.cairosquad.domain.usecase.LoginUseCase
import com.cairosquad.domain.usecase.ThemeManagerUseCase
import com.cairosquad.entity.Theme
import com.cairosquad.viewmodel.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
@HiltViewModel
class MoreViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val themeManagerUseCase: ThemeManagerUseCase,
    private val languageManagerUseCase: LanguageManagerUseCase,
    private val versionUseCase: GetAppVersionUseCase,
    private val account : AccountUseCase
) : BaseViewModel<MoreScreenState, MoreScreenEffect>(
    initialState = MoreScreenState()
), MoreScreenInteractionListener {

    init {
        checkUserLoggedInStatus()
        viewModelScope.launch {
            themeManagerUseCase.getTheme().collectLatest { newTheme ->
                updateState { it.copy(currentTheme = newTheme.toMoreScreenStateTheme()) }
            }
        }
        viewModelScope.launch {
            languageManagerUseCase.getLanguage().collectLatest { newLanguage ->
                updateState { it.copy(currentLanguage = newLanguage.toMoreScreenStateLanguage()) }
            }
        }
        updateState {
            it.copy(appVersion = versionUseCase.getAppVersion())
        }
    }

    private fun checkUserLoggedInStatus() {
        viewModelScope.launch {
            val isUserLoggedIn = loginUseCase.isUserLoggedIn()
            updateState { it.copy(isUserLoggedIn = isUserLoggedIn) }

            // Only fetch account details if the user is logged in
            if (isUserLoggedIn) {
                try {
                    val accountDetails = account.getAccountDetails()
                    updateState {
                        it.copy(
                            userProfileImage = accountDetails.avatarPath,
                            userName = accountDetails.name
                        )
                    }
                } catch (e: Exception) {
                    // Handle potential errors when fetching account details
                    // This prevents crashes when there are API issues
                    updateState {
                        it.copy(
                            userProfileImage = null,
                            userName = ""
                        )
                    }
                }
            }
        }
    }

    override fun onLoginClick() {
        sendEffect(MoreScreenEffect.NavigateToLogin)
    }

    override fun onMyRatingsClick() {
        sendEffect(MoreScreenEffect.NavigateToMyRatings)
    }

    override fun onThemeClick() {
        updateState { it.copy(isThemeBottomSheetOpen = true) }
    }

    override fun onThemeBottomSheetDismiss() {
        updateState { it.copy(isThemeBottomSheetOpen = false) }
    }

    override fun onThemeBottomSheetConfirm(theme: MoreScreenState.Theme) {
        updateState { it.copy(currentTheme = theme) }
        viewModelScope.launch {
            themeManagerUseCase.saveTheme(theme.toDomain())
        }
        onThemeBottomSheetDismiss()
    }


    override fun onLanguageClick() {
        updateState {
            it.copy(isLanguageBottomSheetOpen = true)
        }
    }

    override fun onLanguageBottomSheetConfirm(language: MoreScreenState.Language) {
        updateState { it.copy(currentLanguage = language, isLanguageBottomSheetOpen = false) }
        viewModelScope.launch {
            languageManagerUseCase.saveLanguage(language.toDomainLanguage())
        }
        onLanguageBottomSheetDismiss()
    }

    override fun onLanguageBottomSheetDismiss() {
        updateState { it.copy(isLanguageBottomSheetOpen = false) }
    }

    override fun onLogoutClick() {
        updateState { it.copy(isLogoutButtonVisible = true) }
    }

    override fun onLogoutConfirm() {
        viewModelScope.launch {
            loginUseCase.logout()
            withContext(Dispatchers.IO){
                account.removeAccountDetails()
            }
            checkUserLoggedInStatus()
            updateState { it.copy( isLogoutButtonVisible = false) }
        }
    }

    override fun onLogoutDismiss() {
        updateState { it.copy(isLogoutButtonVisible = false) }
    }
}

private fun Theme.toMoreScreenStateTheme(): MoreScreenState.Theme {
    return MoreScreenState.Theme.valueOf(this.name)
}
