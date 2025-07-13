package com.cairosquad.viewmodel.searchviewmodel

import com.cairosquad.viewmodel.base.BaseInteractionListener

interface SearchInteractionListener : BaseInteractionListener {
    fun onQueryTextChanged(query: String)
    fun onSearch(query: String)
    fun onCancelSearch()
    fun onRecentSearchItemClicked(query: String)
    fun onClearHistory()
    fun onRemoveHistoryItem(query: String)
    fun onBackClicked()
    fun onClickSearchTextField()
    override fun onRefresh()
}