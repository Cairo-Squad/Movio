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

fun getCacheCodeOfSeries(seriesId: Long, language: String): String {
    return "series/seriesId=$seriesId/language=$language"
}

fun getCacheCodeOfTopRatedSeries(page: Int, genreId: Long?, language: String): String {
    return "series/topRated/page=$page/genreId=$genreId/language=$language"
}

fun getCacheCodeOfSimilarSeries(seriesId: Long, page: Int, language: String): String {
    return "series/similar/seriesId=$seriesId/page=$page/language=$language"
}

fun getCacheCodeOfTrendingSeries(page: Int, genreId: Long?, language: String): String {
    return "series/trending/page=$page/genreId=$genreId/language=$language"
}

fun getCacheCodeOfMoreRecommendedSeries(page: Int, genreId: Long?, language: String): String {
    return "series/moreRecommended/page=$page/genreId=$genreId/language=$language"
}

fun getCacheCodeOfFreeToWatchSeries(page: Int, genreId: Long?, language: String): String {
    return "series/freeToWatch/page=$page/genreId=$genreId/language=$language"
}

fun getCacheCodeOfOnTvSeries(page: Int, genreId: Long?, language: String): String {
    return "series/onTv/page=$page/genreId=$genreId/language=$language"
}

fun getCacheCodeOfAiringTodaySeries(page: Int, genreId: Long?, language: String): String {
    return "series/airingToday/page=$page/genreId=$genreId/language=$language"
}

fun getCacheCodeOfSeriesByCategory(page: Int, genreId: Long, language: String): String {
    return "series/byCategory/page=$page/genreId=$genreId/language=$language"
}

fun getCacheCodeOfPopularSeries(page: Int, genreId: Long?, language: String): String {
    return "series/popular/page=$page/genreId=$genreId/language=$language"
}

fun getCacheCodeOfAllSeries(page: Int, genreId: Long?, sortType: SortType?, language: String): String {
    return "series/all/page=$page/genreId=$genreId/sortType=${sortType?.sortBy}/language=$language"
}

fun getCacheCodeOfSearchedSeries(query: String, page: Int, language: String): String {
    return "search/series/page=$page/query=$query/language=$language"
}

fun getCacheCodeOfSeriesReviews(page: Int, seriesId: Long, language: String): String {
    return "series/reviews/page=$page/seriesId=$seriesId/language=$language"
}

fun getCacheCodeOfSeriesOfArtist(artistId: Long, language: String): String {
    return "series/artist/artistId=$artistId/language=$language"
}

fun getCacheCodeOfArtist(artistId: Long, language: String): String {
    return "artists/artistId=$artistId/language=$language"
}

fun getCacheCodeOfArtistsByQuery(query: String, page: Int, language: String): String {
    return "artists/search/page=$page/query=$query/language=$language"
}

fun getCacheCodeOfMovieTopCast(movieId: Long, page: Int, language: String): String {
    return "artists/topCast/movieId=$movieId/page=$page/language=$language"
}

fun getCacheCodeOfSeriesTopCast(seriesId: Long, page: Int, language: String): String {
    return "artists/topCast/seriesId=$seriesId/page=$page/language=$language"
}

fun getCacheCodeOfSeriesSeasons(seriesId: Long, language: String): String {
    return "seasons/seriesId=$seriesId/language=$language"
}

fun getCacheCodeOfEpisodes(seriesId: Long, seasonNumber: Int, language: String): String {
    return "episodes/seriesId=$seriesId/season=$seasonNumber/language=$language"
}