package com.cairosquad.viewmodel.rated

interface MyRatingsInteractionListener {
    fun onBackPressed()
    fun onItemClicked(itemId: Long, isMovie: Boolean)
}