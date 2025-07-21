package com.cairosquad.ui.utils

import androidx.annotation.StringRes
import com.cairosquad.design_system.R
import com.cairosquad.viewmodel.exception.ErrorStatus

@StringRes
fun errorStatusToMessageResource(errorStatus: ErrorStatus): Int {
    return when (errorStatus) {
        ErrorStatus.NO_INTERNET -> R.string.no_internet_connection
        ErrorStatus.NETWORK_ERROR -> R.string.an_error_occured_while_getting_results
        ErrorStatus.UNKNOWN_ERROR -> R.string.an_unexpected_error_occurred
        ErrorStatus.UNAUTHORIZED -> R.string.unauthorized_access
        ErrorStatus.EMPTY -> R.string.no_results_found
        ErrorStatus.PARSING_ERROR -> R.string.error_parsing_data
    }
}