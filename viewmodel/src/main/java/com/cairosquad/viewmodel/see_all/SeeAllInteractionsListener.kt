package com.cairosquad.viewmodel.see_all

interface SeeAllInteractionsListener {
    fun onGenreSelected(genreIndex: Int)
    fun onClickMedia(mediaId: Long, isMovie: Boolean)
    fun onClickBack()
    fun onRefresh(genreIndex: Int)
}