package com.cairosquad.ui.model

import androidx.annotation.DrawableRes

data class BottomNavItem(
    @DrawableRes val unColoredIcon: Int,
    @DrawableRes val coloredIcon: Int,
    val label: String
)
