package com.cairosquad.repository.utils.sharedDto.local

import com.cairosquad.domain.model.SortType
fun getCacheCodeOfMovie(movieId: Long, language: String): String {
    return "movies/movieId=$movieId/language=$language"
}

fun getCacheCodeOfTopRatedMovies(page: Int, genreId: Long?, language: String): String {
    return "movies/topRated/page=$page/genreId=$genreId/language=$language"
}

fun getCacheCodeOfSimilarMovies(movieId: Long, page: Int, language: String): String {
    return "movies/similar/movieId=$movieId/page=$page/language=$language"
}

fun getCacheCodeOfPersonalizedMovies(page: Int, language: String): String {
    return "movies/personalized/page=$page/language=$language"
}

fun getCacheCodeOfSuggestedMovies(language: String): String {
    return "movies/suggested/language=$language"
}

fun getCacheCodeOfUpcomingMovies(page: Int, genreId: Long?, language: String): String {
    return "movies/upcoming/page=$page/genreId=$genreId/language=$language"
}

fun getCacheCodeOfNowPlayingMovies(page: Int, genreId: Long?, language: String): String {
    return "movies/nowPlaying/page=$page/genreId=$genreId/language=$language"
}

fun getCacheCodeOfTrendingMovies(page: Int, genreId: Long?, language: String): String {
    return "movies/trending/page=$page/genreId=$genreId/language=$language"
}

fun getCacheCodeOfMoreRecommendedMovies(page: Int, genreId: Long?, language: String): String {
    return "movies/moreRecommended/page=$page/genreId=$genreId/language=$language"
}

fun getCacheCodeOfFreeToWatchMovies(page: Int, genreId: Long?, language: String): String {
    return "movies/freeToWatch/page=$page/genreId=$genreId/language=$language"
}

fun getCacheCodeOfMoviesByCategory(page: Int, genreId: Long, language: String): String {
    return "movies/byCategory/page=$page/genreId=$genreId/language=$language"
}

fun getCacheCodeOfPopularMovies(page: Int, genreId: Long?, language: String): String {
    return "movies/popular/page=$page/genreId=$genreId/language=$language"
}

fun getCacheCodeOfAllMovies(page: Int, genreId: Long?, sortType: SortType?, language: String): String {
    return "movies/all/page=$page/genreId=$genreId/sortType=${sortType?.sortBy}/language=$language"
}

fun getCacheCodeOfSearchedMovies(query: String, page: Int, language: String): String {
    return "search/movie/page=$page/query=$query/language=$language"
}

fun getCacheCodeOfMovieReviews(page: Int, movieId: Long, language: String): String {
    return "movies/reviews/page=$page/movieId=$movieId/language=$language"
}

fun getCacheCodeOfMoviesOfArtist(artistId: Long, language: String): String {
    return "movies/artist/artistId=$artistId/language=$language"
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

fun getCacheCodeOfSeriesOfArtist(artistId: Long): String {
    return "series/artist/artistId = $artistId"
}
fun getCacheCodeOfArtist(artistId: Long): String {
    return "artists/artistId=$artistId"
}
fun getCacheCodeOfArtistsByQuery(query: String, page: Int): String {
    return "artists/search/page=$page/query=$query"
}

fun getCacheCodeOfMovieTopCast(movieId: Long, page: Int): String {
    return "artists/topCast/movieId=$movieId/page=$page"
}

fun getCacheCodeOfSeriesTopCast(seriesId: Long, page: Int): String {
    return "artists/topCast/seriesId=$seriesId/page=$page"
}
fun getCacheCodeOfSeriesSeasons(seriesId: Long): String {
    return "seasons/seriesId=$seriesId"
}
fun getCacheCodeOfEpisodes(seriesId: Long, seasonNumber: Int): String {
    return "episodes/seriesId=$seriesId/season=$seasonNumber"
}