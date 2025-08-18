package com.cairosquad.viewmodel.search

interface SearchInteractionListener {
    fun onQueryTextChanged(query: String)
    fun onSearch()
    fun onCancelSearch()
    fun onRecentSearchItemClick(query: String)
    fun onClearHistory()
    fun onRemoveHistoryItem(query: String)
    fun onBackClick()
    fun onClickSearchTextField()
    fun onRefresh()
    fun onMovieClick(movieId: Long)
    fun onSeriesClick(seriesId: Long)
    fun onArtistClick(artistId: Long)
    fun onSeeAllForYouClick()
    fun onTabSelected(index: Int)
    fun onTabPagingError(error: Throwable)

}