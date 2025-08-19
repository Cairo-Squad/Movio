package com.cairosquad.viewmodel.login

import androidx.lifecycle.viewModelScope
import com.cairosquad.domain.exception.MovioException
import com.cairosquad.domain.usecase.LoginUseCase
import com.cairosquad.viewmodel.R
import com.cairosquad.viewmodel.base.BaseViewModel
import com.cairosquad.viewmodel.exception.ErrorStatus
import com.cairosquad.viewmodel.exception.exceptionToErrorStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : BaseViewModel<LoginScreenState, LoginEffect>(LoginScreenState()), LoginInteractionListener {

    override fun onUsernameChange(username: String) {
        updateState { it.copy(username = username) }
        resetUsernameValidation()
    }

    override fun onPasswordChange(password: String) {
        updateState { it.copy(password = password) }
        resetPasswordValidation()
    }

    override fun onPasswordVisibilityIconClick() {
        updateState { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    override fun onForgetPasswordClick() {
        sendEffect(LoginEffect.NavigateToForgetPassword)
    }

    override fun onLoginClick() {
        val isValid = validateFields()
        if (!isValid) return

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
        updateState { it.copy(isLoading = false) }
    }

    private fun startLoading() {
        updateState { it.copy(isLoading = true) }
    }

    private fun onLoginSuccess(response: Unit) {
        updateState { it.copy(error = null) }
        sendEffect(LoginEffect.NavigateToHome)
    }

    override fun onContinueAsAGuestClick() {
        sendEffect(LoginEffect.NavigateToGuestHome)
    }

    override fun onSignUpClick() {
        sendEffect(LoginEffect.NavigateToSignUp)
    }

    private fun handleError(throwable: Throwable) {
        val status = if (throwable is MovioException) {
            exceptionToErrorStatus(throwable)
        } else {
            ErrorStatus.UNKNOWN_ERROR
        }

        updateState { it.copy(error = status) }
        showSnackBar(R.string.something_went_wrong, isSuccessful = false)
    }

    fun showSnackBar(messageId: Int, isSuccessful: Boolean, durationMillis: Long = 2000) {
        viewModelScope.launch {
            updateState {
                it.copy(
                    showSnackBar = true,
                    snackMessageId = messageId,
                    isProcessSuccess = isSuccessful
                )
            }
            delay(durationMillis)
            updateState { it.copy(showSnackBar = false) }
        }
    }

    private fun validateFields(): Boolean {
        resetUsernameValidation()
        resetPasswordValidation()

        val isUsernameValid = validateUsername()
        val isPasswordValid = validatePassword()

        return isUsernameValid && isPasswordValid
    }

    private fun validateUsername(): Boolean {
        val username = screenState.value.username
        return when {
            username.isEmpty() -> {
                updateState {
                    it.copy(
                        errors = it.errors.toMutableMap().apply {
                            this[LoginScreenState.FormField.USERNAME] =
                                LoginScreenState.ValidationError.EMPTY_FIELD
                        }
                    )
                }
                false
            }
            username.length < 2 -> {
                updateState {
                    it.copy(
                        errors = it.errors.toMutableMap().apply {
                            this[LoginScreenState.FormField.USERNAME] =
                                LoginScreenState.ValidationError.TOO_SHORT_FIELD
                        }
                    )
                }
                false
            }
            else -> {
                resetUsernameValidation()
                true
            }
        }
    }

    private fun validatePassword(): Boolean {
        val password = screenState.value.password
        return when {
            password.isEmpty() -> {
                updateState {
                    it.copy(
                        errors = it.errors.toMutableMap().apply {
                            this[LoginScreenState.FormField.PASSWORD] =
                                LoginScreenState.ValidationError.EMPTY_FIELD
                        }
                    )
                }
                false
            }
            password.length < 4 -> {
                updateState {
                    it.copy(
                        errors = it.errors.toMutableMap().apply {
                            this[LoginScreenState.FormField.PASSWORD] =
                                LoginScreenState.ValidationError.TOO_SHORT_FIELD
                        }
                    )
                }
                false
            }
            else -> {
                resetPasswordValidation()
                true
            }
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