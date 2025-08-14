package com.cairosquad.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.cairosquad.design_system.R
import com.cairosquad.design_system.theme.Theme

@Composable
fun getSnackBarIcon(isProcessSuccess: Boolean): ImageVector {
    return when (isProcessSuccess) {
        true -> {
            ImageVector.vectorResource(
                if (Theme.isDark)
                    R.drawable.snack_bar_icon_save_success_dark
                else
                    R.drawable.snack_bar_icon_save_success_light
            )
        }
        false -> {
            ImageVector.vectorResource(
                if (Theme.isDark)
                    R.drawable.snack_bar_icon_fail_dark
                else
                    R.drawable.snack_bar_icon_fail_light
            )
        }
    }
}