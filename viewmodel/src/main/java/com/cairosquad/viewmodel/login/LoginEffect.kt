package com.cairosquad.viewmodel.login

sealed interface LoginEffect {
    data object NavigateToForgetPassword : LoginEffect
    data object NavigateToHome : LoginEffect
    data object NavigateToGuestHome : LoginEffect
    data object NavigateToSignUp : LoginEffect
}