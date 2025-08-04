package com.cairosquad.viewmodel.home


import androidx.lifecycle.viewModelScope
import com.cairosquad.domain.exception.MovioException
import com.cairosquad.domain.usecase.AccountUseCase
import com.cairosquad.domain.usecase.ManageMoviesUseCase
import com.cairosquad.domain.usecase.ManageSeriesUseCase
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series
import com.cairosquad.viewmodel.base.BaseViewModel
import com.cairosquad.viewmodel.exception.ErrorStatus
import com.cairosquad.viewmodel.exception.exceptionToErrorStatus
import com.cairosquad.viewmodel.home.HomeScreenState.ScreenStatus
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
        fetchPopularMedia(null)
        loadGenres()
        getAccountDetails()
    }

    private fun getAccountDetails() {
        tryToCall(
            block = { accountUseCase.getAccountDetails() },
            onSuccess = {accountDetails ->

                updateState { it.copy(profileImage = accountDetails.avatarPath) } },
            onError = {}
        )
    }

    private fun setSectionLoading(sectionType: MediaContentType) {
        updateState {
            val newSection = HomeScreenState.SectionUiState(isLoading = true)
            it.copy(
                sections = (it.sections + (sectionType to newSection))
                    .entries.sortedBy { entry -> entry.key.ordinal }
                    .associate { it -> it.key to it.value }
            )
        }
    }

    suspend fun getDataOfSection(
        sectionType: MediaContentType,
        genreId: Long? = null
    ): Pair<List<Movie>, List<Series>> {
        val page = 1

        val movies: List<Movie> = when (sectionType) {
            MediaContentType.TOP_RATING -> manageMoviesUseCase.getTopRatingMovies(page, genreId)
            MediaContentType.TRENDING -> manageMoviesUseCase.getTrendingMovies(page, genreId)
            MediaContentType.FREE_TO_WATCH -> manageMoviesUseCase.getFreeToWatchMovies(page, genreId)
            MediaContentType.UPCOMING -> manageMoviesUseCase.getUpcomingMovies(page, genreId)
            MediaContentType.NOW_PLAYING -> manageMoviesUseCase.getNowPlayingMovies(page, genreId)
            MediaContentType.MORE_RECOMMENDED -> manageMoviesUseCase.getMoreRecommendedMovies(page, genreId)
            else -> emptyList()
        }

        val series: List<Series> = when (sectionType) {
            MediaContentType.TOP_RATING -> manageSeriesUseCase.getTopRatingSeries(page, genreId)
            MediaContentType.TRENDING -> manageSeriesUseCase.getTrendingSeries(page, genreId)
            MediaContentType.UPCOMING -> manageSeriesUseCase.getFreeToWatchSeries(page, genreId)
            MediaContentType.MORE_RECOMMENDED -> manageSeriesUseCase.getMoreRecommendedSeries(page, genreId)
            MediaContentType.AIRING_TODAY -> manageSeriesUseCase.getAiringTodaySeries(page, genreId)
            MediaContentType.ON_TV -> manageSeriesUseCase.getOnTvSeries(page, genreId)
            else -> emptyList()
        }

        return Pair(movies, series)
    }
    private fun fetchPopularMedia(genreId: Long? = null) {
        tryToCall(
            block = { fetchPopularMediaBlock(genreId) },
            onSuccess = ::onSuccessFetchPopularMedia,
            onError = ::handleError
        )
    }

    // TODO edit media type and get data in home screen for every one in here screen
    private suspend fun fetchPopularMediaBlock(genreId: Long? = null): Pair<List<Movie>, List<Series>> {
        val series = manageSeriesUseCase.getPopularSeries(
            page = 1,
            genreId = genreId
        )
        val movies = manageMoviesUseCase.getPopularMovies(
            page = 1,
            genreId = genreId
        )
        return Pair(movies, series)
    }

    private fun onSuccessFetchPopularMedia(moviesAndSeries: Pair<List<Movie>, List<Series>>) {
        updateState {
            it.copy(
                popularMovies = moviesAndSeries.first.map(Movie::toHomeMediaUiState),
                popularSeries = moviesAndSeries.second.map(Series::toHomeMediaUiState),
                isRefreshing = false,
                screenStatus = ScreenStatus.SUCCESS
            )
        }
    }

    private fun loadGenres() {
        tryToCall(
            block = ::loadGenresBlock,
            onSuccess = { genres -> updateState { it.copy(genres = genres.toList()) } },
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

    override fun onGenreSelected(genreIndex: Int) {
        fetchMediaByCategory(screenState.value.genres[genreIndex].id)
        updateState {
            it.copy(
                selectedGenreIndex = genreIndex
            )
        }
    }

    override fun onClickTab(tabIndex: Int) {
        if (tabIndex == HomeScreenState.Tab.CATEGORIES.ordinal) {
            fetchMediaByCategory()
        }
        updateState {
            it.copy(selectedTab = HomeScreenState.Tab.entries[tabIndex])
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

    override fun onSortingSelected(filter: HomeScreenState.SortingType) {
        if (filter == screenState.value.selectedSortingType) return
        updateState {
            it.copy(selectedSortingType = filter)
        }
        sortCategoriesMedia()
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
                        unifiedMediaPager.getCombinedMedia(genreId = genre.id)
                    }

                    HomeScreenState.SortingType.LATEST -> {
                        unifiedMediaPager.getCombinedMedia(genreId = genre.id)
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

    override fun onSectionVisible(sectionType: MediaContentType) {
        if (screenState.value.sections.containsKey(sectionType)) return
        fetchSectionData(sectionType)
    }


    private fun fetchSectionData(
        sectionType: MediaContentType,
    ) {
        setSectionLoading(sectionType)
        tryToCall(
            block = { getDataOfSection(sectionType) },
            onSuccess = { onSuccessFetchData(it, sectionType) },
            onError = ::handleError
        )

    }

    private fun onSuccessFetchData(
        moviesAndSeries: Pair<List<Movie>, List<Series>>,
        sectionType: MediaContentType
    ) {
        val section = HomeScreenState.SectionUiState(
            movies = moviesAndSeries.first.map(Movie::toHomeMediaUiState),
            series = moviesAndSeries.second.map(Series::toHomeMediaUiState)
        )
        updateState {
            it.copy(
                sections = (it.sections + (sectionType to section))
                    .entries.sortedBy { entry -> entry.key.ordinal }
                    .associate { entry -> entry.key to entry.value }
            )
        }
    }

    private fun handleError(throwable: Throwable) {
        updateState {
            it.copy(
                errorStatus = handleHomeException(throwable),
                screenStatus = ScreenStatus.FAILED,
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

    override fun onRefresh() {
        updateState { it.copy(isRefreshing = true, screenStatus = ScreenStatus.LOADING) }
        loadHomeScreenData()
        screenState.value.sections.forEach {
            fetchSectionData(it.key)
        }
        viewModelScope.launch {
            delay(500L)
            updateState { it.copy(isRefreshing = false) }
        }
    }
}