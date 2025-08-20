package com.cairosquad.viewmodel.more

sealed interface MoreScreenEffect {
    object NavigateToMyRatings : MoreScreenEffect
    object NavigateToLogin : MoreScreenEffect
    object NavigateToLoginAfterLogout : MoreScreenEffect
}