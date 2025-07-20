package com.cairosquad.viewmodel.login

import com.cairosquad.domain.usecase.authentication.LoginUseCase
import com.cairosquad.viewmodel.base.BaseViewModel

class LoginViewModel(
    private val loginUseCase: LoginUseCase
) : BaseViewModel<LoginScreenState, LoginEffect>(LoginScreenState()), LoginInteractionListener {
    override fun onUsernameChange(username: String) {
        updateState {
            it.copy(
                username = username
            )
        }
    }

    override fun onPasswordChange(password: String) {
        updateState {
            it.copy(
                password = password
            )
        }
    }

    override fun onPasswordVisibilityIconClick() {
        updateState {
            it.copy(
                isPasswordVisible = !it.isPasswordVisible
            )
        }
    }

    override fun onForgetPasswordClick() {
        sendEffect(LoginEffect.NavigateToForgetPassword)
    }

    override fun onLoginClick() {
        tryToCall(
            block = {
                loginUseCase.login(
                    username = screenState.value.username,
                    password = screenState.value.password
                )
            },
            onSuccess = {
                saveToken(it)
            },
            onError = {
                handleError(it)
            },
            onStart = {
                updateState {
                    it.copy(
                        isLoading = true
                    )
                }
            },
            onEnd = {
                updateState {
                    it.copy(
                        isLoading = false
                    )
                }
            }
        )
    }

    private fun saveToken(token: String) {
        tryToCall(
            block = {
                loginUseCase.saveToken(token)
            },
            onSuccess = {
                sendEffect(LoginEffect.NavigateToHome)
            },
            onError = {
                handleError(it)
            },
            onStart = {
                updateState {
                    it.copy(
                        isLoading = true
                    )
                }
            },
            onEnd = {
                updateState {
                    it.copy(
                        isLoading = false
                    )
                }
            }
        )
    }

    override fun onContinueAsAGuestClick() {
        sendEffect(LoginEffect.NavigateToGuestHome)
    }

    override fun onSignUpClick() {
        sendEffect(LoginEffect.NavigateToSignUp)
    }


    private fun handleError(
        throwable: Throwable
    ) {
        // TODO
    }
}