package com.cairosquad.repository.utils.sharedDto.local

import com.cairosquad.domain.model.SortType

fun getRequestOfMovie(movieId: Long): String {
    return "movies/movieId = $movieId"
}
fun getRequestOfTopRatedMovies(page: Int, genreId: String?): String {
    return "movies/tobRated/page = $page/genreId = $genreId"
}
fun getRequestOfSimilarMovies(movieId: Long, page: Int): String {
    return "movies/tobRated/movieId = $movieId/page = $page"
}
fun getRequestOfPersonalizedMovies(page: Int): String {
    return "movies/personalized/page = $page"
}
fun getRequestOfSuggestedMovies(): String {
    return "movies/suggested"
}
fun getRequestOfUpcomingMovies(page: Int, genreId: String?): String {
    return "movies/upcoming/page=$page/genreId=$genreId"
}
fun getRequestOfNowPlayingMovies(page: Int, genreId: String?): String {
    return "movies/nowPlaying/page=$page/genreId=$genreId"
}
fun getRequestOfTrendingMovies(page: Int, genreId: String?): String {
    return "movies/trending/page=$page/genreId=$genreId"
}
fun getRequestOfMoreRecommendedMovies(page: Int, genreId: String?): String {
    return "movies/moreRecommended/page=$page/genreId=$genreId"
}
fun getRequestOfFreeToWatchMovies(page: Int, genreId: String?): String {
    return "movies/freeToWatch/page=$page/genreId=$genreId"
}
fun getRequestOfMoviesByCategory(page: Int, genreId: String): String {
    return "movies/byCategory/page=$page/genreId=$genreId"
}
fun getRequestOfPopularMovies(page: Int, genreId: String?): String {
    return "movies/popular/page=$page/genreId=$genreId"
}
fun getRequestOfAllMovies(page: Int, genreId: String?, sortType: SortType?): String {
    return "movies/all/page=$page/genreId=$genreId/sortType=${sortType?.sortBy}"
}
fun getRequestOfSearchedMovies(query: String, page: Int): String {
    return "search/movie/page=$page/query=$query"
}
fun getRequestOfMovieReviews(page: Int, movieId: Long): String {
    return "movies/tobRated/page = $page/movieId = $movieId"
}
fun getRequestOfSeries(seriesId: Long): String {
    return "series/seriesId = $seriesId"
}
fun getRequestOfTopRatedSeries(page: Int, genreId: String?): String {
    return "series/tobRated/page = $page/genreId = $genreId"
}
fun getRequestOfSimilarSeries(seriesId: Long, page: Int): String {
    return "series/tobRated/seriesId = $seriesId/page = $page"
}
fun getRequestOfTrendingSeries(page: Int, genreId: String?): String {
    return "series/trending/page=$page/genreId=$genreId"
}
fun getRequestOfMoreRecommendedSeries(page: Int, genreId: String?): String {
    return "series/moreRecommended/page=$page/genreId=$genreId"
}
fun getRequestOfFreeToWatchSeries(page: Int, genreId: String?): String {
    return "series/freeToWatch/page=$page/genreId=$genreId"
}
fun getRequestOfOnTvSeries(page: Int, genreId: String?): String {
    return "series/onTv/page=$page/genreId=$genreId"
}
fun getRequestOfAiringTodaySeries(page: Int, genreId: String?): String {
    return "series/airingToday/page=$page/genreId=$genreId"
}
fun getRequestOfSeriesByCategory(page: Int, genreId: String): String {
    return "series/byCategory/page=$page/genreId=$genreId"
}
fun getRequestOfPopularSeries(page: Int, genreId: String?): String {
    return "series/popular/page=$page/genreId=$genreId"
}
fun getRequestOfAllSeries(page: Int, genreId: String?, sortType: SortType?): String {
    return "series/all/page=$page/genreId=$genreId/sortType=${sortType?.sortBy}"
}
fun getRequestOfSearchedSeries(query: String, page: Int): String {
    return "search/series/page=$page/query=$query"
}
fun getRequestOfSeriesReviews(page: Int, seriesId: Long): String {
    return "series/tobRated/page = $page/seriesId = $seriesId"
}