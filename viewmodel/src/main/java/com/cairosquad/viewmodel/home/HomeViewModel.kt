package com.cairosquad.viewmodel.home


import androidx.lifecycle.viewModelScope
import com.cairosquad.domain.exception.MovioException
import com.cairosquad.domain.model.SortType
import com.cairosquad.domain.usecase.AccountUseCase
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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val manageMoviesUseCase: ManageMoviesUseCase,
    private val manageSeriesUseCase: ManageSeriesUseCase,
    private val accountUseCase: AccountUseCase,
    private val unifiedMediaPager: UnifiedMediaPager
) : BaseViewModel<HomeScreenState, HomeEffect>(initialState = HomeScreenState()),
    HomeInteractionsListener {

    init {
        loadHomeScreenData()
    }

    fun loadHomeScreenData() {
        fetchPopularMovies(null)
        fetchAllMovieSectionsOrdered()
        getAccountDetails()
    }

    override fun onRefresh() {
        updateState { it.copy(isRefreshing = true, dataRequestStatus = DataRequestStatus.LOADING) }
        when (screenState.value.selectedTab) {
            HomeScreenState.Tab.MOVIES -> {
                fetchPopularMovies(null)
                fetchAllMovieSectionsOrdered()
            }

            HomeScreenState.Tab.TV_SHOWS -> {
                fetchPopularSeries(null)
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

    override fun onClickTab(tabIndex: Int) {
        when (tabIndex) {
            0 -> {
                fetchPopularMovies(null)
                fetchAllMovieSectionsOrdered()
            }
            1 -> {
                fetchPopularSeries(null)
                fetchAllSeriesSectionsOrdered()
            }
            2 -> {
                loadGenres()
                fetchMediaByCategory()
            }
        }
        updateState {
            it.copy(selectedTab = HomeScreenState.Tab.entries[tabIndex])
        }
    }

    override fun onGenreSelected(genreIndex: Int) {
        fetchMediaByCategory(screenState.value.genres[genreIndex].id)
        updateState {
            it.copy(
                selectedGenreIndex = genreIndex
            )
        }
    }

    override fun onSortingSelected(filter: HomeScreenState.SortingType) {
        if (filter == screenState.value.selectedSortingType) return
        updateState {
            it.copy(selectedSortingType = filter)
        }
        sortCategoriesMedia()
    }

    private fun fetchPopularMovies(genreId: Long? = null) {
        fetchPopularMedia(
            genreId = genreId,
            fetchBlock = { id -> manageMoviesUseCase.getPopularMovies(page = 1, genreId = id) },
            mapper = Movie::toHomeMediaUiState,
            onSuccess = { mappedList ->
                updateState {
                    it.copy(
                        popularMovies = mappedList,
                        isRefreshing = false,
                        dataRequestStatus = DataRequestStatus.SUCCESS
                    )
                }
            })
    }

    private fun fetchPopularSeries(genreId: Long? = null) {
        fetchPopularMedia(
            genreId = genreId,
            fetchBlock = { id -> manageSeriesUseCase.getPopularSeries(page = 1, genreId = id) },
            mapper = Series::toHomeMediaUiState,
            onSuccess = { mappedList ->
                updateState {
                    it.copy(
                        popularSeries = mappedList,
                        isRefreshing = false,
                        dataRequestStatus = DataRequestStatus.SUCCESS
                    )
                }
            })
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
                            topRating = topRating,
                            nowPlaying = nowPlaying,
                            upComing = upComing,
                            moreRecommended = moreRecommended
                        ),
                        dataRequestStatus = HomeScreenState.DataRequestStatus.SUCCESS
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
                            topRating = topRating,
                            airingToday = airingToday,
                            onTv = onTv,
                            moreRecommended = moreRecommended
                        ),
                        dataRequestStatus = HomeScreenState.DataRequestStatus.SUCCESS
                    )
                }
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    private fun fetchMediaByCategory(genreId: Long? = null) {
        tryToCall(
            block = {
                unifiedMediaPager.getCombinedMedia(genreId)
            },
            onSuccess = { media ->
                updateState {
                    it.copy(
                        categoriesMedia = media
                    )
                }
            },
            onError = ::handleError
        )
    }

    private fun sortCategoriesMedia() {
        val genre = screenState.value.genres[screenState.value.selectedGenreIndex]
        tryToCall(
            block = {
                when (screenState.value.selectedSortingType) {
                    HomeScreenState.SortingType.ALL -> {
                        unifiedMediaPager.getCombinedMedia(genreId = genre.id)
                    }

                    HomeScreenState.SortingType.POPULARITY -> {
                        unifiedMediaPager.getCombinedMedia(genreId = genre.id, SortType.POPULAR)
                    }

                    HomeScreenState.SortingType.LATEST -> {
                        unifiedMediaPager.getCombinedMedia(genreId = genre.id, SortType.LATEST)
                    }
                }
            },
            onSuccess = { media ->
                updateState {
                    it.copy(categoriesMedia = media)
                }
            },
            onError = ::handleError
        )

    }

    private suspend fun loadGenresBlock(): List<HomeScreenState.GenreUiState> {
        val movieGenres = manageMoviesUseCase.getMoviesGenres()
        val seriesGenres = manageSeriesUseCase.getSeriesGenres()

        return buildSet {
            add(HomeScreenState.GenreUiState.defaultGenre)
            movieGenres.mapTo(this) { it.toHomeGenreUiState() }
            seriesGenres.mapTo(this) { it.toHomeGenreUiState() }
        }.toList()
    }

    private fun loadGenres() {
        tryToCall(
            block = ::loadGenresBlock,
            onSuccess = { genres -> updateState { it.copy(genres = genres.toList()) } },
            onError = ::handleError
        )
    }

    private fun getAccountDetails() {
        tryToCall(
            block = { accountUseCase.getAccountDetails() },
            onSuccess = { accountDetails ->

                updateState { it.copy(profileImage = accountDetails.avatarPath) }
            },
            onError = {}
        )
    }

    override fun onClickProfile() {
        sendEffect(HomeEffect.NavigateToProfile)
    }

    override fun onClickMedia(mediaId: Long, isMovie: Boolean) {
        sendEffect(
            HomeEffect.NavigateMediaDetails(
                mediaId = mediaId,
                isMovie = isMovie
            )
        )
    }

    override fun onClickSeeAll(
        mediaContentType: MediaContentType,
        mediaType: MediaType
    ) {
        sendEffect(
            HomeEffect.NavigateToSeeAllScreen(
                mediaContentType,
                mediaType = mediaType
            )
        )
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