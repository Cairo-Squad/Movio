package com.cairosquad.viewmodel.search

interface SearchInteractionListener {
    fun onQueryTextChanged(query: String)
    fun onSearch()
    fun onCancelSearch()
    fun onRecentSearchItemClicked(query: String)
    fun onClearHistory()
    fun onRemoveHistoryItem(query: String)
    fun onBackClicked()
    fun onClickSearchTextField()
    fun onRefresh()
    fun onMovieClicked(movieId: Long)
    fun onSeriesClicked(seriesId: Long)
    fun onArtistClicked(artistId: Long)
    fun onSeeAllForYouClicked()
    fun onTabSelected(index: Int)
}