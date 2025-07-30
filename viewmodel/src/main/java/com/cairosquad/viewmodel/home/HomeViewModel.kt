package com.cairosquad.viewmodel.home


import com.cairosquad.domain.exception.MovioException
import com.cairosquad.domain.model.SortType
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
import com.cairosquad.viewmodel.util.combineTwoList

class HomeViewModel(
    private val manageMoviesUseCase: ManageMoviesUseCase,
    private val manageSeriesUseCase: ManageSeriesUseCase
) : BaseViewModel<HomeScreenState, HomeEffect>(initialState = HomeScreenState()),
    HomeInteractionsListener {

    init {
        loadHomeScreenData()
    }

    fun loadHomeScreenData() {
        fetchPopularMedia(null)
        loadGenres()
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
        return when (sectionType) {
            MediaContentType.TOP_RATING -> {
                Pair(
                    manageMoviesUseCase.getTopRatingMovies(
                        page = 1,
                        genreId = genreId
                    ),
                    manageSeriesUseCase.getTopRatingSeries(
                        page = 1,
                        genreId = genreId
                    ),
                )
            }

            MediaContentType.TRENDING -> {
                Pair(
                    manageMoviesUseCase.getTrendingMovies(
                        page = 1,
                        genreId = genreId
                    ),
                    manageSeriesUseCase.getTrendingSeries(
                        page = 1,
                        genreId = genreId
                    ),
                )
            }

            MediaContentType.FREE_TO_WATCH -> {
                Pair(
                    manageMoviesUseCase.getFreeToWatchMovies(
                        page = 1,
                        genreId = genreId
                    ),
                    emptyList()
                )
            }

            MediaContentType.UPCOMING -> {
                Pair(
                    manageMoviesUseCase.getUpcomingMovies(
                        page = 1,
                        genreId = genreId
                    ),
                    manageSeriesUseCase.getFreeToWatchSeries(
                        page = 1,
                        genreId = genreId
                    )
                )
            }

            MediaContentType.NOW_PLAYING -> {
                Pair(
                    manageMoviesUseCase.getNowPlayingMovies(
                        page = 1,
                        genreId = genreId
                    ),
                    emptyList(),
                )
            }

            MediaContentType.MORE_RECOMMENDED -> {
                Pair(
                    manageMoviesUseCase.getMoreRecommendedMovies(
                        page = 1,
                        genreId = genreId
                    ),
                    manageSeriesUseCase.getMoreRecommendedSeries(
                        page = 1,
                        genreId = genreId
                    ),
                )
            }

            MediaContentType.AIRING_TODAY -> {
                Pair(
                    emptyList(),
                    manageSeriesUseCase.getAiringTodaySeries(
                        page = 1,
                        genreId = genreId
                    ),
                )
            }

            MediaContentType.ON_TV -> {
                Pair(
                    emptyList(),
                    manageSeriesUseCase.getOnTvSeries(page = 1, genreId = genreId),
                )
            }
        }
    }

    private fun fetchPopularMedia(genreId: Long? = null) {
        tryToCall(
            block = { fetchPopularMediaBlock(genreId) },
            onSuccess = ::onSuccessFetchPopularMedia,
            onError = ::handleError
        )
    }

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


    override fun onClickTab(tabIndex: Int) {

        if (tabIndex == HomeScreenState.Tab.CATEGORIES.ordinal) {
            fetchMediaByCategory()
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

    private fun fetchMediaByCategory(genreId: Long? = null) {
        tryToCall(
            block = {
                Pair(
                    manageMoviesUseCase.getAllMovies(page = 1, genreId = genreId),
                    manageSeriesUseCase.getAllSeries(page = 1, genreId = genreId)
                )
            },
            onSuccess = { (movies, series) ->
                updateState {
                    it.copy(
                        categoriesMedia = movies.map(Movie::toHomeMediaUiState) + series.map(Series::toHomeMediaUiState)
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

    override fun onSectionVisible(sectionType: MediaContentType) {
        if (screenState.value.sections.containsKey(sectionType)) return
        fetchSectionData(sectionType)
    }

    private fun sortCategoriesMedia() {
        val genre = screenState.value.genres[screenState.value.selectedGenreIndex]
        tryToCall(
            block = { when (screenState.value.selectedSortingType) {
                HomeScreenState.SortingType.ALL -> {
                     Pair(
                        manageMoviesUseCase.getAllMovies(page = 1, genreId = genre.id),
                        manageSeriesUseCase.getAllSeries(page = 1, genreId = genre.id)
                    )
                }
                HomeScreenState.SortingType.POPULARITY -> {
                    Pair(
                        manageMoviesUseCase.getAllMovies(page = 1, genreId = genre.id,
                            SortType.POPULAR),
                        manageSeriesUseCase.getAllSeries(page = 1, genreId = genre.id,
                            SortType.POPULAR)
                    )
                }
                HomeScreenState.SortingType.LATEST -> {
                    Pair(
                        manageMoviesUseCase.getAllMovies(page = 1, genreId = genre.id,
                            SortType.LATEST),
                        manageSeriesUseCase.getAllSeries(page = 1, genreId = genre.id,
                            SortType.LATEST)
                    )
                }
            }},
            onSuccess = { (movies, series) ->
                updateState {
                    it.copy(
                        categoriesMedia = combineTwoList(
                            list1 = movies.map(Movie::toHomeMediaUiState),
                            list2 = series.map(Series::toHomeMediaUiState)
                        ),

                        )
                }
            },
            onError = ::handleError
        )

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

    fun onRetry() {
        updateState { it.copy(isRefreshing = true) }
        loadAllData()
    }

    fun onRefresh() {
        updateState { it.copy(isRefreshing = true) }
        loadAllData()
    }



}