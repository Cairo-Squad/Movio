package com.cairosquad.viewmodel.see_all

interface SeeAllInteractionsListener {
    fun onGenreSelected(genreIndex: Int)
    fun onMediaClick(mediaId: Long, isMovie: Boolean)
    fun onBackClick()
    fun onRefresh(genreIndex: Int)
}