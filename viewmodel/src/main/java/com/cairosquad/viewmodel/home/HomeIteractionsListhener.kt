package com.cairosquad.viewmodel.home

import com.cairosquad.viewmodel.util.MediaContentType
import com.cairosquad.viewmodel.util.MediaType

interface HomeInteractionsListener {
    fun onClickProfile()
    fun onClickMedia(mediaId: Long, isMovie: Boolean)
    fun onClickSeeAll(mediaContentType: MediaContentType, mediaType: MediaType)
    fun onClickTab(tabIndex: Int)
    fun onGenreSelected(genreIndex: Int)
    fun onSortingSelected(filter: HomeScreenState.SortingType)
    fun onSectionVisible(sectionType: MediaContentType)
    fun onRefresh()
}