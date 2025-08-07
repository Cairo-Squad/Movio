package com.cairosquad.viewmodel.details.movie

import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewModelScope
import com.cairosquad.domain.exception.MovioException
import com.cairosquad.domain.usecase.AccountUseCase
import com.cairosquad.domain.usecase.LoginUseCase
import com.cairosquad.domain.usecase.ManageMoviesUseCase
import com.cairosquad.entity.Artist
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Review
import com.cairosquad.viewmodel.R
import com.cairosquad.viewmodel.base.BaseViewModel
import com.cairosquad.viewmodel.details.movie.MovieScreenState.ScreenStatus
import com.cairosquad.viewmodel.exception.ErrorStatus
import com.cairosquad.viewmodel.exception.exceptionToErrorStatus
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = MovieViewModel.Factory::class)
class MovieViewModel @AssistedInject constructor(
    private val movieUseCase: ManageMoviesUseCase,
    private val loginUseCase: LoginUseCase,
    private val accountUseCase: AccountUseCase,
    @Assisted private val movieId: Long = 0
) : BaseViewModel<MovieScreenState, MovieEffect>(MovieScreenState()),
    MovieInteractionListener {

    @AssistedFactory
    interface Factory {
        fun create(movieId: Long): MovieViewModel
    }

    init {
        loadMovieData(movieId)
        addMovieToHistory(movieId)
    }

    private fun loadMovieData(movieId: Long) {
        getBasicDetails(movieId)
        getActors(movieId)
        getReviews(movieId)
        getSimilarMovies(movieId)
    }

    private fun addMovieToHistory(movieId: Long) {
        tryToCall(
            block = { accountUseCase.addMovieToHistory(movieId) },
            onSuccess = {},
            onError = {}
        )
    }

    private fun getBasicDetails(movieId: Long) {
        tryToCall(
            onStart = {
                updateState { it.copy(basicDetailsSectionState = ScreenStatus.LOADING) }
            },
            block = { movieUseCase.getMovieById(movieId) },
            onSuccess = ::setBasicDetailsToUiState,
            onError = { throwable ->
                setError(throwable) { copy(basicDetailsSectionState = ScreenStatus.ERROR) }
            },
            dispatcher = Dispatchers.IO
        )
    }

    private fun setBasicDetailsToUiState(movie: Movie) {
        updateState {
            it.copy(
                basicDetailsSectionState = ScreenStatus.SUCCESS,
                movie = movie.toMovieUiState()
            )
        }
    }

    private fun getActors(movieId: Long) {
        tryToCall(
            onStart = {
                updateState { it.copy(castSectionState = ScreenStatus.LOADING) }
            },
            block = { movieUseCase.getMovieTopCast(movieId) },
            onSuccess = ::setActors,
            onError = { throwable ->
                setError(throwable) { copy(castSectionState = ScreenStatus.ERROR) }
            },
            dispatcher = Dispatchers.IO
        )
    }

    private fun setActors(actors: List<Artist>) {
        updateState {
            it.copy(
                castSectionState = ScreenStatus.SUCCESS,
                topCast = actors.map { it.toArtistUiState() }
            )
        }
    }

    private fun getReviews(movieId: Long) {
        tryToCall(
            onStart = {
                updateState { it.copy(reviewsSectionState = ScreenStatus.LOADING) }
            },
            block = { movieUseCase.getMovieReviews(movieId) },
            onSuccess = ::setReviews,
            onError = { throwable ->
                setError(throwable) { copy(reviewsSectionState = ScreenStatus.ERROR) }
            },
            dispatcher = Dispatchers.IO
        )
    }

    private fun setReviews(reviews: List<Review>) {
        updateState {
            it.copy(
                reviewsSectionState = ScreenStatus.SUCCESS,
                reviews = reviews.map { it.toReviewUiState() }
            )
        }
    }

    private fun getSimilarMovies(movieId: Long) {
        tryToCall(
            onStart = {
                updateState { it.copy(similarMoviesSectionState = ScreenStatus.LOADING) }
            },
            block = { movieUseCase.getSimilarMovies(movieId) },
            onSuccess = ::setSimilarMovies,
            onError = { throwable ->
                setError(throwable) { copy(similarMoviesSectionState = ScreenStatus.ERROR) }
            },
            dispatcher = Dispatchers.IO
        )
    }

    private fun setSimilarMovies(movies: List<Movie>) {
        updateState {
            it.copy(
                similarMoviesSectionState = ScreenStatus.SUCCESS,
                similarMovies = movies.map { it.toMovieUiState() }
            )
        }
    }

    override fun onBackClick() {
        sendEffect(MovieEffect.NavigateBack)
    }

    override fun onShareClick() {
        updateState { it.copy(isShareBottomSheetOpen = true) }
    }

    override fun onFavoriteClick() {
        tryToCall(
            block = loginUseCase::isUserLoggedIn,
            onSuccess = { authed ->
                if (!authed) {
                    updateState { it.copy(isNoAccountBottomSheetOpen = true) }
                } else {
                    addToFavorite(movieId)
                }
            },
            onError = {}
        )
    }

    private fun addToFavorite(movieId: Long) {
        tryToCall(
            block = { accountUseCase.addMovieToFavorite(movieId) },
            onSuccess = {
                viewModelScope.launch {
                    updateState {
                        it.copy(
                            showSnackBar = true, isProcessSuccess = true,
                            snackMessage = stringResource(R.string.movie_favorite_success)
                        )
                    }
                    delay(2000)
                    updateState { it.copy(showSnackBar = false) }
                }
            },
            onError = {
                viewModelScope.launch {
                    updateState {
                        it.copy(
                            showSnackBar = true,
                            isProcessSuccess = false,
                            snackMessage = stringResource(R.string.movie_favorite_fail)
                        )
                    }
                    delay(2000)
                    updateState { it.copy(showSnackBar = false) }
                }
            }
        )
    }

    override fun onRateItClick() {
        tryToCall(
            block = loginUseCase::isUserLoggedIn,
            onSuccess = { authed ->
                if (!authed) {
                    updateState { it.copy(isNoAccountBottomSheetOpen = true) }
                } else {
                    updateState { it.copy(isRateBottomSheetOpen = true) }
                }
            },
            onError = {}
        )
    }

    override fun onPlayClick() {
        sendEffect(MovieEffect.PlayTrailer)
    }

    override fun onAddToListClick() {
        tryToCall(
            block = loginUseCase::isUserLoggedIn,
            onSuccess = { authed ->
                if (!authed) {
                    updateState { it.copy(isNoAccountBottomSheetOpen = true) }
                } else {
                    loadMovieLists()
                }
            },
            onError = {}
        )
    }

    private fun loadMovieLists() {
        tryToCall(
            block = { accountUseCase.getMoviesLists(1) },
            onSuccess = { mediaLists ->
                val uiLists = mediaLists.map { list -> list.toUiState() }
                updateState {
                    it.copy(isAddToListBottomSheetOpen = true, moviesLists = uiLists)
                }
            },
            onError = {},
            dispatcher = Dispatchers.Main
        )
    }

    override fun onCreateListClicked() {
        updateState {
            it.copy(
                isAddToListBottomSheetOpen = false,
                showCreateListBottomSheet = true
            )
        }
    }

    override fun onClickList(id: Long) {
        tryToCall(
            block = {
                val movies = accountUseCase.getMoviesOfList(id, 1)
                if (screenState.value.movie.id in movies.map { it.id }) {
                    false
                } else {
                    accountUseCase.addMovieToList(id, screenState.value.movie.id)
                    true
                }
            },
            onSuccess = { isAdded ->
                updateState {
                    it.copy(
                        isAddToListBottomSheetOpen = false,
                        isProcessSuccess = isAdded,
                        showSnackBar = true,
                        snackMessage = if (isAdded) "Added to list" else "Movie already in list"
                    )
                }
                delay(2000L)
                updateState {
                    it.copy(
                        showSnackBar = false,
                        snackMessage = "",
                        isProcessSuccess = isAdded
                    )
                }
            },
            onError = {
                updateState {
                    it.copy(
                        isAddToListBottomSheetOpen = false,
                        isProcessSuccess = false,
                        showSnackBar = true,
                        snackMessage = "Error adding movie to list"
                    )
                }
                delay(2000L)
                updateState {
                    it.copy(
                        showSnackBar = false,
                        snackMessage = "",
                        isProcessSuccess = false
                    )
                }
            }
        )
    }

    override fun onSubmitCreateListClicked() {
        tryToCall(
            block = {
                accountUseCase.createList(screenState.value.listName)
                accountUseCase.getMoviesLists(1)
            },
            onSuccess = { moviesLists ->
                updateState {
                    it.copy(
                        showCreateListBottomSheet = false,
                        isAddToListBottomSheetOpen = true,
                        listName = "",
                        moviesLists = moviesLists.map { list -> list.toUiState() }
                    )
                }
            },
            onError = {
                updateState {
                    it.copy(
                        showCreateListBottomSheet = false,
                        isAddToListBottomSheetOpen = true,
                        listName = "",
                    )
                }
                delay(2000L)
                updateState {
                    it.copy(
                        showSnackBar = false,
                        snackMessage = "",
                        isProcessSuccess = false
                    )
                }
            }
        )
    }

    override fun onDismissCreateListBottomSheet() {
        updateState { it.copy(showCreateListBottomSheet = false) }
    }

    override fun onListValueChange(listName: String) {
        updateState { it.copy(listName = listName) }
    }

    override fun onSeeAllCastClick(movieId: Long) {
        sendEffect(MovieEffect.NavigateToAllActors(movieId))
    }

    override fun onActorClick(actorId: Long) {
        sendEffect(MovieEffect.NavigateToActor(actorId))
    }

    override fun onSeeAllReviewsClick(movieId: Long) {
        sendEffect(MovieEffect.NavigateToAllReviews(movieId))
    }

    override fun onSeeAllSimilarMoviesClick(movieId: Long) {
        sendEffect(MovieEffect.NavigateToSimilarMovies(movieId))
    }

    override fun onMovieClick(movieId: Long) {
        sendEffect(MovieEffect.NavigateToMovie(movieId))
    }

    override fun onCopy(message: String, isSuccessful: Boolean) {
        tryToCall(
            onStart = {
                onDismissShareBottomSheet()
                delay(500)
                updateState {
                    it.copy(
                        showSnackBar = true,
                        snackMessage = message,
                        isProcessSuccess = isSuccessful
                    )
                }
            },
            block = { delay(2000) },
            onSuccess = {
                updateState {
                    it.copy(
                        showSnackBar = false,
                        snackMessage = message
                    )
                }
            },
            onError = {},
        )
    }

    override fun onDismissShareBottomSheet() {
        updateState { it.copy(isShareBottomSheetOpen = false) }
    }

    override fun onDismissLoginBottomSheet() {
        updateState { it.copy(isNoAccountBottomSheetOpen = false) }
    }

    override fun onDismissRateBottomSheet() {
        updateState { it.copy(isRateBottomSheetOpen = false) }
    }

    override fun onDismissAddToListBottomSheet() {
        updateState { it.copy(isAddToListBottomSheetOpen = false) }
    }

    override fun onRateChange(rate: Int) {
        updateState { it.copy(rate = rate) }
    }

    override fun onSubmitRateClicked(rate: Int) {
        updateState { it.copy(isRateBottomSheetOpen = false) }
    }

    override fun onNavigateToLogin() {
        sendEffect(MovieEffect.NavigateToLogin)
    }

    override fun onRefresh() {
        loadMovieData(movieId)
    }

    private fun setError(
        throwable: Throwable,
        updateSection: MovieScreenState.() -> MovieScreenState
    ) {
        updateState {
            it.updateSection().copy(
                errorStatus = handleDetailsException(throwable)
            )
        }
    }

    fun handleDetailsException(e: Throwable): ErrorStatus {
        return when (e) {
            is MovioException -> {
                exceptionToErrorStatus(e)
            }

            else -> ErrorStatus.UNKNOWN_ERROR
        }
    }
}