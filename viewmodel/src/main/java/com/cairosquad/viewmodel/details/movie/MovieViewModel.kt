package com.cairosquad.viewmodel.details.movie

import com.cairosquad.domain.exception.MovioException
import com.cairosquad.domain.usecase.LoginUseCase
import com.cairosquad.domain.usecase.ManageMoviesUseCase
import com.cairosquad.entity.Artist
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Review
import com.cairosquad.viewmodel.base.BaseViewModel
import com.cairosquad.viewmodel.details.movie.MovieScreenState.ScreenStatus
import com.cairosquad.viewmodel.exception.ErrorStatus
import com.cairosquad.viewmodel.exception.exceptionToErrorStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val movieUseCase: ManageMoviesUseCase,
    private val loginUseCase: LoginUseCase,
) : BaseViewModel<MovieScreenState, MovieEffect>(MovieScreenState()),
    MovieInteractionListener {

    private val movieId: Long = 0 // TODO: get from savedHandle

    init {
        loadMovieData(movieId)
    }

    private fun loadMovieData(movieId: Long) {
        getBasicDetails(movieId)
        getActors(movieId)
        getReviews(movieId)
        getSimilarMovies(movieId)
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
                }
            },
            onError = {}
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
                    updateState { it.copy(isAddToListBottomSheetOpen = true) }
                }
            },
            onError = {}
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