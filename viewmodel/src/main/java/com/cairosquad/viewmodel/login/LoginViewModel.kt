package com.cairosquad.viewmodel.login

import com.cairosquad.domain.exception.MovioException
import com.cairosquad.domain.usecase.authentication.LoginUseCase
import com.cairosquad.viewmodel.base.BaseViewModel
import com.cairosquad.viewmodel.exception.ErrorStatus
import com.cairosquad.viewmodel.exception.exceptionToErrorStatus

class LoginViewModel(
    private val loginUseCase: LoginUseCase
) : BaseViewModel<LoginScreenState, LoginEffect>(LoginScreenState()), LoginInteractionListener {
    override fun onUsernameChange(username: String) {
        updateState {
            it.copy(
                username = username
            )
        }

        resetUsernameValidation()
    }

    override fun onPasswordChange(password: String) {
        updateState {
            it.copy(
                password = password
            )
        }

        resetPasswordValidation()
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
        validateFields()
        tryToCall(
            block = {
                loginUseCase.login(
                    username = screenState.value.username,
                    password = screenState.value.password
                )
            },
            onSuccess = ::onLoginSuccess,
            onError = ::handleError,
            onStart = ::startLoading,
            onEnd = ::endLoading
        )
    }

    private fun endLoading() {
        updateState {
            it.copy(
                isLoading = false
            )
        }
    }

    private fun startLoading() {
        updateState {
            it.copy(
                isLoading = true
            )
        }
    }

    private fun onLoginSuccess(response: Unit) {
        updateState {
            it.copy(
                error = null
            )
        }
        sendEffect(LoginEffect.NavigateToHome)
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
        if (throwable is MovioException) {
            updateState {
                it.copy(
                    error = exceptionToErrorStatus(throwable)
                )
            }
        } else {
            updateState {
                it.copy(
                    error = ErrorStatus.UNKNOWN_ERROR
                )
            }
        }
    }

    private fun validateFields() {
        resetUsernameValidation()
        resetPasswordValidation()

        validateUsername()
        validatePassword()
    }

    private fun validateUsername() {
        val username = screenState.value.username
        if (username.isEmpty()) {
            updateState {
                it.copy(
                    errors = it.errors.toMutableMap().apply {
                        this[LoginScreenState.FormField.USERNAME] =
                            LoginScreenState.ValidationError.EMPTY_FIELD
                    }
                )
            }
        } else if (username.length < 2) {
            updateState {
                it.copy(
                    errors = it.errors.toMutableMap().apply {
                        this[LoginScreenState.FormField.USERNAME] =
                            LoginScreenState.ValidationError.TOO_SHORT_FIELD
                    }
                )
            }
        } else {
            resetUsernameValidation()
        }
    }

    private fun validatePassword() {
        val password = screenState.value.password

        if (password.isEmpty()) {
            updateState {
                it.copy(
                    errors = it.errors.toMutableMap().apply {
                        this[LoginScreenState.FormField.PASSWORD] =
                            LoginScreenState.ValidationError.EMPTY_FIELD
                    }
                )
            }
        } else if (password.length < 8) {
            updateState {
                it.copy(
                    errors = it.errors.toMutableMap().apply {
                        this[LoginScreenState.FormField.PASSWORD] =
                            LoginScreenState.ValidationError.TOO_SHORT_FIELD
                    }
                )
            }
        } else {
            resetPasswordValidation()
        }
    }

    private fun resetUsernameValidation() {
        updateState {
            it.copy(
                errors = it.errors.toMutableMap().apply {
                    this[LoginScreenState.FormField.USERNAME] = null
                }
            )
        }
    }

    private fun resetPasswordValidation() {
        updateState {
            it.copy(
                errors = it.errors.toMutableMap().apply {
                    this[LoginScreenState.FormField.PASSWORD] = null
                }
            )
        }
    }
}