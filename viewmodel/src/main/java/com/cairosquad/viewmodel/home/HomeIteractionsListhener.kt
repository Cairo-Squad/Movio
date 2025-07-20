package com.cairosquad.viewmodel.home

interface HomeInteractionsListener {
    fun onClickProfile()
    fun onClickCategory(category: String)
    fun onClickMovie(movieId: Long)
    fun onClickSeries(seriesId: Long)
    fun onClickSeeAllTopRated(isMovie : Boolean )
    fun onClickSeeAllTrending()
    fun onClickSeeAllFreeToWatch()
    fun onClickSeeAllUpcoming()
    fun onClickSeeAllMoreRecommended(isMovie : Boolean)
    fun onClickSeeAllAiringToday()
    fun onClickSeeAllOnTv()
}