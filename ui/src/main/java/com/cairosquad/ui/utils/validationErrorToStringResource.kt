package com.cairosquad.ui.utils

import androidx.annotation.StringRes
import com.cairosquad.ui.R
import com.cairosquad.viewmodel.login.LoginScreenState

@StringRes
fun validationErrorToStringResource(
    error: LoginScreenState.ValidationError,
    field: LoginScreenState.FormField
): Int {
    return when (error) {
        LoginScreenState.ValidationError.EMPTY_FIELD -> R.string.this_field_cannot_be_empty
        LoginScreenState.ValidationError.TOO_SHORT_FIELD -> {
            when (field) {
                LoginScreenState.FormField.USERNAME -> R.string.username_must_be_at_least_2_characters_long
                LoginScreenState.FormField.PASSWORD -> R.string.password_must_be_at_least_8_characters_long
            }
        }
    }
}