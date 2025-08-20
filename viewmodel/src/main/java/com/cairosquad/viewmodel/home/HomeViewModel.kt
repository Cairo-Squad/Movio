package com.cairosquad.viewmodel.home


import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.cairosquad.domain.exception.MovioException
import com.cairosquad.domain.model.SortType
import com.cairosquad.domain.usecase.AccountUseCase
import com.cairosquad.domain.usecase.LoginUseCase
import com.cairosquad.domain.usecase.ManageMoviesUseCase
import com.cairosquad.domain.usecase.ManageSeriesUseCase
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series
import com.cairosquad.viewmodel.base.BaseViewModel
import com.cairosquad.viewmodel.exception.ErrorStatus
import com.cairosquad.viewmodel.exception.exceptionToErrorStatus
import com.cairosquad.viewmodel.home.HomeScreenState.DataRequestStatus
import com.cairosquad.viewmodel.util.MediaContentType
import com.cairosquad.viewmodel.util.MediaType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val manageMoviesUseCase: ManageMoviesUseCase,
    private val manageSeriesUseCase: ManageSeriesUseCase,
    private val accountUseCase: AccountUseCase,
    private val unifiedMediaPager: UnifiedMediaPager,
    private val loginUseCase: LoginUseCase
) : BaseViewModel<HomeScreenState, HomeEffect>(initialState = HomeScreenState()),
    HomeInteractionsListener {

    init {
        loadHomeScreenData()
    }

    fun loadHomeScreenData() {
        fetchPopularMovies()
        fetchAllMovieSectionsOrdered()
        getAccountDetails()
    }

    override fun onRefresh() {
        updateState { it.copy(isRefreshing = true, dataRequestStatus = DataRequestStatus.LOADING) }
        when (screenState.value.selectedTab) {
            HomeScreenState.Tab.MOVIES -> {
                fetchPopularMovies()
                fetchAllMovieSectionsOrdered()
            }

            HomeScreenState.Tab.TV_SHOWS -> {
                fetchPopularSeries()
                fetchAllSeriesSectionsOrdered()
            }

            HomeScreenState.Tab.CATEGORIES -> {
                loadGenres()
                fetchMediaByCategory()
            }
        }
        viewModelScope.launch {
            delay(500L)
            updateState { it.copy(isRefreshing = false) }
        }
    }

    override fun onTabClick(tabIndex: Int) {
        when (tabIndex) {
            TAB_MOVIES -> {
                fetchPopularMovies()
                fetchAllMovieSectionsOrdered()
            }

            TAB_SERIES -> {
                fetchPopularSeries()
                fetchAllSeriesSectionsOrdered()
            }

            TAB_CATEGORIES -> {
                loadGenres()
                fetchMediaByCategory()
            }
        }
        updateState { it.copy(selectedTab = HomeScreenState.Tab.entries[tabIndex]) }
    }

    override fun onGenreSelected(genreIndex: Int) {
        fetchMediaByCategory(screenState.value.genres[genreIndex].id)
        updateState { it.copy(selectedGenreIndex = genreIndex) }
    }

    override fun onSortingSelected(filter: HomeScreenState.SortingType) {
        if (filter == screenState.value.selectedSortingType) return
        updateState { it.copy(selectedSortingType = filter) }
        sortCategoriesMedia()
    }

    private fun fetchPopularMovies() {
        fetchPopularMedia(
            fetchBlock = { id -> manageMoviesUseCase.getPopularMovies(1, id) },
            mapper = Movie::toUiState,
            onSuccess = ::onPopularMoviesSuccess
        )
    }

    private fun fetchPopularSeries() {
        fetchPopularMedia(
            fetchBlock = { id -> manageSeriesUseCase.getPopularSeries(1, id) },
            mapper = Series::toUiState,
            onSuccess = ::onPopularSeriesSuccess
        )
    }

    private fun onPopularMoviesSuccess(movies: List<HomeScreenState.MediaUiState>) {
        updateState {
            it.copy(
                popularMovies = movies,
                isRefreshing = false,
                dataRequestStatus = DataRequestStatus.SUCCESS
            )
        }
    }

    private fun onPopularSeriesSuccess(series: List<HomeScreenState.MediaUiState>) {
        updateState {
            it.copy(
                popularSeries = series,
                isRefreshing = false,
                dataRequestStatus = DataRequestStatus.SUCCESS
            )
        }
    }

    private fun fetchAllMovieSectionsOrdered() {
        viewModelScope.launch {
            try {
                val topRating = manageMoviesUseCase.getTopRatingMovies(1)
                val nowPlaying = manageMoviesUseCase.getNowPlayingMovies(1)
                val upComing = manageMoviesUseCase.getUpcomingMovies(1)
                val moreRecommended = manageMoviesUseCase.getMoreRecommendedMovies(1)
                updateState {
                    it.copy(
                        movieSections = it.movieSections.copy(
                            topRating = topRating.map(Movie::toUiState),
                            nowPlaying = nowPlaying.map(Movie::toUiState),
                            upComing = upComing.map(Movie::toUiState),
                            moreRecommended = moreRecommended.map(Movie::toUiState)
                        ),
                        dataRequestStatus = DataRequestStatus.SUCCESS
                    )
                }
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    private fun fetchAllSeriesSectionsOrdered() {
        viewModelScope.launch {
            try {
                val topRating = manageSeriesUseCase.getTopRatingSeries(1)
                val airingToday = manageSeriesUseCase.getAiringTodaySeries(1)
                val onTv = manageSeriesUseCase.getOnTvSeries(1)
                val moreRecommended = manageSeriesUseCase.getMoreRecommendedSeries(1)

                updateState {
                    it.copy(
                        seriesSections = it.seriesSections.copy(
                            topRating = topRating.map(Series::toUiState),
                            airingToday = airingToday.map(Series::toUiState),
                            onTv = onTv.map(Series::toUiState),
                            moreRecommended = moreRecommended.map(Series::toUiState)
                        ),
                        dataRequestStatus = DataRequestStatus.SUCCESS
                    )
                }
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    private fun fetchMediaByCategory(genreId: Long? = null) {
        tryToCall(
            block = { unifiedMediaPager.getCombinedMedia(genreId) },
            onSuccess = ::onCategoryMediaSuccess,
            onError = ::handleError
        )
    }

    private fun onCategoryMediaSuccess(media: Flow<PagingData<HomeScreenState.MediaUiState>>) {
        updateState { it.copy(categoriesMedia = media) }
    }

    private fun sortCategoriesMedia() {
        val genre = screenState.value.genres[screenState.value.selectedGenreIndex]
        tryToCall(
            block = {
                when (screenState.value.selectedSortingType) {
                    HomeScreenState.SortingType.ALL ->
                        unifiedMediaPager.getCombinedMedia(genre.id)

                    HomeScreenState.SortingType.POPULARITY ->
                        unifiedMediaPager.getCombinedMedia(genre.id, SortType.POPULAR)

                    HomeScreenState.SortingType.LATEST ->
                        unifiedMediaPager.getCombinedMedia(genre.id, SortType.LATEST)
                }
            },
            onSuccess = ::onCategoryMediaSuccess,
            onError = ::handleError
        )
    }

    private suspend fun loadGenresBlock(): List<HomeScreenState.GenreUiState> {
        val movieGenres = manageMoviesUseCase.getMoviesGenres()
        val seriesGenres = manageSeriesUseCase.getSeriesGenres()

        return buildSet {
            add(HomeScreenState.GenreUiState.defaultGenre)
            movieGenres.mapTo(this) { it.toUiState() }
            seriesGenres.mapTo(this) { it.toUiState() }
        }.toList()
    }

    private fun loadGenres() {
        tryToCall(
            block = ::loadGenresBlock,
            onSuccess = { genres -> updateState { it.copy(genres = genres) } },
            onError = ::handleError
        )
    }

    private fun getAccountDetails() {
        tryToCall(
            block = { accountUseCase.getAccountDetails() },
            onSuccess = { accountDetails -> updateState { it.copy(profileImage = accountDetails.avatarPath) } },
            onError = {}
        )
    }

    override fun onProfileClick() {
        tryToCall(
            block = { loginUseCase.isUserLoggedIn() },
            onSuccess = ::navigateToProfileOrLogin,
            onError = {}
        )
    }

    private fun navigateToProfileOrLogin(isUserLoggedIn: Boolean) {
        if (isUserLoggedIn) sendEffect(HomeEffect.NavigateToProfile)
        else sendEffect(HomeEffect.NavigateToLogin)
    }

    override fun onMediaClick(mediaId: Long, isMovie: Boolean) {
        sendEffect(HomeEffect.NavigateMediaDetails(mediaId, isMovie))
    }

    override fun onSeeAllClick(mediaContentType: MediaContentType, mediaType: MediaType) {
        sendEffect(HomeEffect.NavigateToSeeAllScreen(mediaContentType, mediaType))
    }

    private fun <T> fetchPopularMedia(
        genreId: Long? = null,
        fetchBlock: suspend (Long?) -> List<T>,
        mapper: (T) -> HomeScreenState.MediaUiState,
        onSuccess: (List<HomeScreenState.MediaUiState>) -> Unit
    ) {
        tryToCall(
            block = { fetchBlock(genreId) },
            onSuccess = { list -> onSuccess(list.map(mapper)) },
            onError = ::handleError
        )
    }

    private fun handleError(throwable: Throwable) {
        updateState {
            it.copy(
                errorStatus = handleHomeException(throwable),
                dataRequestStatus = DataRequestStatus.FAILED,
                isRefreshing = false
            )
        }
    }

    private fun handleHomeException(e: Throwable): ErrorStatus {
        return when (e) {
            is MovioException -> exceptionToErrorStatus(e)
            else -> ErrorStatus.UNKNOWN_ERROR
        }
    }

    companion object {
        const val HORIZONTAL_PAGER_COUNT = 7
        private const val TAB_MOVIES = 0
        private const val TAB_SERIES = 1
        private const val TAB_CATEGORIES = 2
        val homePageMoviesSections = listOf(
            MediaContentType.TOP_RATING,
            MediaContentType.NOW_PLAYING,
            MediaContentType.UPCOMING,
            MediaContentType.MORE_RECOMMENDED
        )
        val homePageSeriesSections = listOf(
            MediaContentType.TOP_RATING,
            MediaContentType.AIRING_TODAY,
            MediaContentType.ON_TV,
            MediaContentType.MORE_RECOMMENDED
        )
    }
}