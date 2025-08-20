package com.cairosquad.viewmodel.home

import com.cairosquad.viewmodel.util.MediaContentType
import com.cairosquad.viewmodel.util.MediaType

interface HomeInteractionsListener {
    fun onProfileClick()
    fun onMediaClick(mediaId: Long, isMovie: Boolean)
    fun onSeeAllClick(mediaContentType: MediaContentType, mediaType: MediaType)
    fun onTabClick(tabIndex: Int)
    fun onGenreSelected(genreIndex: Int)
    fun onSortingSelected(filter: HomeScreenState.SortingType)
    fun onRefresh()
}