package com.cairosquad.design_system.component

import androidx.annotation.DrawableRes

data class BottomNavItem(
    @DrawableRes val unColoredIcon: Int,
    @DrawableRes val coloredIcon: Int,
    val label: String
)