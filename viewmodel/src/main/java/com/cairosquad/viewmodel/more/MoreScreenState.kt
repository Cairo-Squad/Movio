package com.cairosquad.viewmodel.more

import androidx.annotation.StringRes
import com.cairosquad.viewmodel.R

data class MoreScreenState(
    val isThemeBottomSheetOpen : Boolean = false,
    val isLanguageBottomSheetOpen : Boolean = false,
    val isUserLoggedIn: Boolean = false,
    val userProfileImage :String?=null,
    val userName :String = "",
    val currentTheme : Theme = Theme.DARK,
    val currentLanguage : Language = Language("ar","Arabic"),
    val appVersion : String = "",
    val isLogoutButtonVisible : Boolean = false
){
    enum class Theme(@StringRes val stringResId: Int) {
        DARK(R.string.dark_mode),
        LIGHT(R.string.light_mode);
    }

    data class Language(val code: String, val name: String)
}
