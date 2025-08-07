package com.cairosquad.viewmodel.more


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
    enum class Theme {
        DARK,LIGHT;
        companion object{
            //create function to convert theme to string
            fun Theme.toThemeString(): String {
                return when (this) {
                    DARK -> "Dark"
                    LIGHT -> "Light"
                }
            }
        }
    }

    data class Language(val code: String, val name: String)

}
