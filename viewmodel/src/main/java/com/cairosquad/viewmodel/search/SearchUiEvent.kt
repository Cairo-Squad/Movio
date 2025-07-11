package com.cairosquad.viewmodel.search

sealed class SearchUiEvent {
    data class ShowToast(val message: String) : SearchUiEvent()
}