package com.cairosquad.viewmodel.login

interface LoginInteractionListener {
    fun onUsernameChange(username: String)
    fun onPasswordChange(password: String)
    fun onPasswordVisibilityIconClick()
    fun onForgetPasswordClick()
    fun onLoginClick()
    fun onContinueAsAGuestClick()
    fun onSignUpClick()
}