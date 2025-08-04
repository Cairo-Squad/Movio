package com.cairosquad.viewmodel.library

import com.cairosquad.viewmodel.base.BaseViewModel

class LibraryViewModel() :
	BaseViewModel<LibraryScreenState, LibraryEffect>(LibraryScreenState()),
	LibraryInteractionListener {

}