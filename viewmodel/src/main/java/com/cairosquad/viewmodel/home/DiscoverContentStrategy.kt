import com.cairosquad.viewmodel.home.HomeScreenState
import com.cairosquad.viewmodel.util.MediaType

interface DiscoverContentStrategy {
    val title: String
    val mediaType: MediaType

    fun getItems(state: HomeScreenState): List<Any>
}

fun filterByMediaType(
    movieList: List<HomeScreenState.MediaUiState>,
    seriesList: List<HomeScreenState.MediaUiState>,
    mediaType: MediaType
): List<Any> {
    return movieList
//    when (mediaType) {
//        MediaType.Movies -> movieList
//        MediaType.Series -> seriesList
//        MediaType.All -> movieList + seriesList
//    }
}

class TopRatedStrategy(override val mediaType: MediaType) : DiscoverContentStrategy {
    override val title = "Top Rating"

    override fun getItems(state: HomeScreenState): List<Any> {
        return filterByMediaType(
            movieList = emptyList(),// state.topRatingMovies,
            seriesList = emptyList(),// state.topRatingSeries,
            mediaType = mediaType
        )
    }
}

class TrendingStrategy(override val mediaType: MediaType) : DiscoverContentStrategy {
    override val title = "Trending"

    override fun getItems(state: HomeScreenState): List<Any> {
        return filterByMediaType(
            movieList = emptyList(),// state.trendingMovies,
            seriesList = emptyList(),// state.airingTodaySeries,
            mediaType = mediaType
        )
    }
}

class MoreRecommendedStrategy(override val mediaType: MediaType) : DiscoverContentStrategy {
    override val title = "More Recommended"

    override fun getItems(state: HomeScreenState): List<Any> {
        return filterByMediaType(
            movieList = emptyList(),// state.moreRecommendedMovies,
            seriesList = emptyList(),// state.moreRecommendedSeries,
            mediaType = mediaType
        )
    }
}

class FreeToWatchStrategy(override val mediaType: MediaType) : DiscoverContentStrategy {
    override val title = "Free To Watch"

    override fun getItems(state: HomeScreenState): List<Any> {
        return filterByMediaType(
            movieList = emptyList(),// state.freeToWatchMovies,
            seriesList = emptyList(),// state.onTvSeries,
            mediaType = mediaType
        )
    }
}

class UpComingStrategy(override val mediaType: MediaType) : DiscoverContentStrategy {
    override val title = "Upcoming"

    override fun getItems(state: HomeScreenState): List<Any> {
        return filterByMediaType(
            movieList = emptyList(),// state.upcomingMovies,
            seriesList = emptyList(),// state.onTvSeries,
            mediaType = mediaType
        )
    }
}