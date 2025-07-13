package com.cairosquad.viewmodel.searchviewmodel

sealed class SearchEffect {
    data class ShowToast(val message: String) : SearchEffect()
}