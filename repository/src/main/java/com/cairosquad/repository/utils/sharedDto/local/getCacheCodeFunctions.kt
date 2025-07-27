package com.cairosquad.repository.utils.sharedDto.local

import com.cairosquad.domain.model.SortType

fun getCacheCodeOfMovie(movieId: Long): String {
    return "movies/movieId = $movieId"
}
fun getCacheCodeOfTopRatedMovies(page: Int, genreId: Long?): String {
    return "movies/tobRated/page = $page/genreId = $genreId"
}
fun getCacheCodeOfSimilarMovies(movieId: Long, page: Int): String {
    return "movies/tobRated/movieId = $movieId/page = $page"
}
fun getCacheCodeOfPersonalizedMovies(page: Int): String {
    return "movies/personalized/page = $page"
}
fun getCacheCodeOfSuggestedMovies(): String {
    return "movies/suggested"
}
fun getCacheCodeOfUpcomingMovies(page: Int, genreId: Long?): String {
    return "movies/upcoming/page=$page/genreId=$genreId"
}
fun getCacheCodeOfNowPlayingMovies(page: Int, genreId: Long?): String {
    return "movies/nowPlaying/page=$page/genreId=$genreId"
}
fun getCacheCodeOfTrendingMovies(page: Int, genreId: Long?): String {
    return "movies/trending/page=$page/genreId=$genreId"
}
fun getCacheCodeOfMoreRecommendedMovies(page: Int, genreId: Long?): String {
    return "movies/moreRecommended/page=$page/genreId=$genreId"
}
fun getCacheCodeOfFreeToWatchMovies(page: Int, genreId: Long?): String {
    return "movies/freeToWatch/page=$page/genreId=$genreId"
}
fun getCacheCodeOfMoviesByCategory(page: Int, genreId: Long): String {
    return "movies/byCategory/page=$page/genreId=$genreId"
}
fun getCacheCodeOfPopularMovies(page: Int, genreId: Long?): String {
    return "movies/popular/page=$page/genreId=$genreId"
}
fun getCacheCodeOfAllMovies(page: Int, genreId: Long?, sortType: SortType?): String {
    return "movies/all/page=$page/genreId=$genreId/sortType=${sortType?.sortBy}"
}
fun getCacheCodeOfSearchedMovies(query: String, page: Int): String {
    return "search/movie/page=$page/query=$query"
}
fun getCacheCodeOfMovieReviews(page: Int, movieId: Long): String {
    return "movies/tobRated/page = $page/movieId = $movieId"
}
fun getCacheCodeOfSeries(seriesId: Long): String {
    return "series/seriesId = $seriesId"
}
fun getCacheCodeOfTopRatedSeries(page: Int, genreId: Long?): String {
    return "series/tobRated/page = $page/genreId = $genreId"
}
fun getCacheCodeOfSimilarSeries(seriesId: Long, page: Int): String {
    return "series/tobRated/seriesId = $seriesId/page = $page"
}
fun getCacheCodeOfTrendingSeries(page: Int, genreId: Long?): String {
    return "series/trending/page=$page/genreId=$genreId"
}
fun getCacheCodeOfMoreRecommendedSeries(page: Int, genreId: Long?): String {
    return "series/moreRecommended/page=$page/genreId=$genreId"
}
fun getCacheCodeOfFreeToWatchSeries(page: Int, genreId: Long?): String {
    return "series/freeToWatch/page=$page/genreId=$genreId"
}
fun getCacheCodeOfOnTvSeries(page: Int, genreId: Long?): String {
    return "series/onTv/page=$page/genreId=$genreId"
}
fun getCacheCodeOfAiringTodaySeries(page: Int, genreId: Long?): String {
    return "series/airingToday/page=$page/genreId=$genreId"
}
fun getCacheCodeOfSeriesByCategory(page: Int, genreId: Long): String {
    return "series/byCategory/page=$page/genreId=$genreId"
}
fun getCacheCodeOfPopularSeries(page: Int, genreId: Long?): String {
    return "series/popular/page=$page/genreId=$genreId"
}
fun getCacheCodeOfAllSeries(page: Int, genreId: Long?, sortType: SortType?): String {
    return "series/all/page=$page/genreId=$genreId/sortType=${sortType?.sortBy}"
}
fun getCacheCodeOfSearchedSeries(query: String, page: Int): String {
    return "search/series/page=$page/query=$query"
}
fun getCacheCodeOfSeriesReviews(page: Int, seriesId: Long): String {
    return "series/tobRated/page = $page/seriesId = $seriesId"
}