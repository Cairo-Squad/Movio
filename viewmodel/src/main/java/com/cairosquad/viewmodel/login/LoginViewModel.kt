package com.cairosquad.viewmodel.login

import com.cairosquad.viewmodel.base.BaseViewModel

class LoginViewModel(

) : BaseViewModel<LoginScreenState, LoginEffect>(LoginScreenState()), LoginInteractionListener {
    override fun onUsernameChange(username: String) {
        TODO("Not yet implemented")
    }

    override fun onPasswordChange(password: String) {
        TODO("Not yet implemented")
    }

    override fun onPasswordVisibilityIconClick() {
        TODO("Not yet implemented")
    }

    override fun onForgetPasswordClick() {
        TODO("Not yet implemented")
    }

    override fun onLoginClick() {
        TODO("Not yet implemented")
    }

    override fun onContinueAsAGuestClick() {
        TODO("Not yet implemented")
    }

    override fun onSignUpClick() {
        TODO("Not yet implemented")
    }

}