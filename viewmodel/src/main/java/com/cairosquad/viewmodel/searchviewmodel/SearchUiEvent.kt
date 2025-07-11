package com.cairosquad.viewmodel.searchviewmodel

sealed class SearchUiEvent {
    data class ShowToast(val message: String) : SearchUiEvent()
}