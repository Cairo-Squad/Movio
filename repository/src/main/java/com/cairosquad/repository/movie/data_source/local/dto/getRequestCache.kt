package com.cairosquad.repository.movie.data_source.local.dto

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