package com.cairosquad.viewmodel.searchviewmodel

interface SearchInteractionListener {
    fun onQueryTextChanged(query: String)
    fun onSearch(query: String)
    fun onCancelSearch()
    fun onRecentSearchItemClicked(query: String)
    fun onClearHistory()
    fun onRemoveHistoryItem(query: String)
    fun onBackClicked()
    fun onClickSearchTextField()
    fun onRefresh()
}