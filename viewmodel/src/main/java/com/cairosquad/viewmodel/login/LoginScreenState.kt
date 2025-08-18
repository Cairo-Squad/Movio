package com.cairosquad.viewmodel.login

import com.cairosquad.viewmodel.exception.ErrorStatus

data class LoginScreenState(
    val username: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val errors: Map<FormField, ValidationError?> = emptyMap(),
    val error: ErrorStatus? = null,
    val showSnackBar: Boolean = false,
    val snackMessageId: Int = 0,
    val isProcessSuccess: Boolean = false,
) {
    enum class FormField {
        USERNAME,
        PASSWORD
    }

    enum class ValidationError {
        EMPTY_FIELD,
        TOO_SHORT_FIELD,
    }
}
