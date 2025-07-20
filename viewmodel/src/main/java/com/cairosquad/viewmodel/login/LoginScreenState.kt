package com.cairosquad.viewmodel.login

data class LoginScreenState(
    val username: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val errors: Map<FormField, String> = emptyMap(),
) {
    enum class FormField {
        USERNAME,
        PASSWORD
    }

    enum class ValidationError {
        EMPTY_FIELD,
        INVALID_USERNAME,
        INVALID_PASSWORD,
        CREDENTIALS_MISMATCH
    }
}
