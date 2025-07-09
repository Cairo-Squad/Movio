package searchviewmodel

interface SearchInteractionListener {
    fun onQueryTextChanged(query: String)
    fun onSearch(query: String)
    fun onHistoryItemClicked(query: String)
    fun onClearHistory()
    fun onRemoveHistoryItem(query: String)
}