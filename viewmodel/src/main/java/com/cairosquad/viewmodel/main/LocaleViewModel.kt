package com.cairosquad.viewmodel.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cairosquad.domain.usecase.LanguageManagerUseCase
import com.cairosquad.domain.usecase.ThemeManagerUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocaleViewModel @Inject constructor(
    private val themeManagerUseCase: ThemeManagerUseCase,
    private val languageManagerUseCase: LanguageManagerUseCase
) : ViewModel() {
    private val _currentTheme = MutableStateFlow(com.cairosquad.viewmodel.main.Theme.DARK)
    val currentTheme: StateFlow<com.cairosquad.viewmodel.main.Theme> = _currentTheme.asStateFlow()


    init {
        viewModelScope.launch {
            themeManagerUseCase.getTheme().collectLatest {
                _currentTheme.value = it.toUi()
            }
        }

//        viewModelScope.launch {
//            .collectLatest {
//                _currentLanguage.value = it.toUi()
//            }
//        }
    }

    fun getLanguageCode() = languageManagerUseCase.getLanguage().map { it.code }
}