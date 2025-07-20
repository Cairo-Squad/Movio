package com.cairosquad.viewmodel.home

interface HomeIteractionsListhener {
    fun onClickProfile()
    fun onClickCategory(category: String)
    fun onClickFeaturedMovie(movieId: Long)
    fun onClickMovie(movieId: Long)
    fun onClickSeries(seriesId: Long)
    fun onClickSeeAllTopRated()
    fun onClickSeeAllTrending()
    fun onClickSeeAllFreeToWatch()
    fun onClickSeeAllUpcoming()
    fun onClickSeeAllMoreRecommended()
    fun onClickSeeAllAiringToday()
    fun onClickSeeAllOnTv()
}