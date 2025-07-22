import com.cairosquad.viewmodel.home.HomeScreenState

interface DiscoverContentStrategy {
    val title: String
    val mediaType: MediaType

    fun getItems(state: HomeScreenState): List<Any>
}

enum class MediaType {
    Movies,
    Series,
    All
}

fun filterByMediaType(
    movieList: List<HomeScreenState.MovieUiState>,
    seriesList: List<HomeScreenState.SeriesUiState>,
    mediaType: MediaType
): List<Any> {
    return when (mediaType) {
        MediaType.Movies -> movieList
        MediaType.Series -> seriesList
        MediaType.All -> movieList + seriesList
    }
}

class TopRatedStrategy(override val mediaType: MediaType) : DiscoverContentStrategy {
    override val title = "Top Rating"

    override fun getItems(state: HomeScreenState): List<Any> {
        return filterByMediaType(
            movieList = state.topRatingMovies,
            seriesList = state.topRatingSeries,
            mediaType = mediaType
        )
    }
}

class TrendingStrategy(override val mediaType: MediaType) : DiscoverContentStrategy {
    override val title = "Trending"

    override fun getItems(state: HomeScreenState): List<Any> {
        return filterByMediaType(
            movieList = state.trendingMovies,
            seriesList = state.airingTodaySeries,
            mediaType = mediaType
        )
    }
}

class MoreRecommendedStrategy(override val mediaType: MediaType) : DiscoverContentStrategy {
    override val title = "More Recommended"

    override fun getItems(state: HomeScreenState): List<Any> {
        return filterByMediaType(
            movieList = state.moreRecommendedMovies,
            seriesList = state.moreRecommendedSeries,
            mediaType = mediaType
        )
    }
}

class FreeToWatchStrategy(override val mediaType: MediaType) : DiscoverContentStrategy {
    override val title = "Free To Watch"

    override fun getItems(state: HomeScreenState): List<Any> {
        return filterByMediaType(
            movieList = state.freeToWatchMovies,
            seriesList = state.onTvSeries,
            mediaType = mediaType
        )
    }
}

class UpComingStrategy(override val mediaType: MediaType) : DiscoverContentStrategy {
    override val title = "Upcoming"

    override fun getItems(state: HomeScreenState): List<Any> {
        return filterByMediaType(
            movieList = state.upcomingMovies,
            seriesList = state.onTvSeries,
            mediaType = mediaType
        )
    }
}