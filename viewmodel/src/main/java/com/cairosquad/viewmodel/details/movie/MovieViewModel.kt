package com.cairosquad.viewmodel.details.movie

import androidx.lifecycle.viewModelScope
import com.cairosquad.domain.exception.MovioException
import com.cairosquad.domain.usecase.AccountUseCase
import com.cairosquad.domain.usecase.GetRatedItemsUseCase
import com.cairosquad.domain.usecase.LoginUseCase
import com.cairosquad.domain.usecase.ManageMoviesUseCase
import com.cairosquad.entity.Artist
import com.cairosquad.entity.MediaList
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
    private val getRatedItemsUseCase: GetRatedItemsUseCase,
    @Assisted private val movieId: Long = 0
) : BaseViewModel<MovieScreenState, MovieEffect>(MovieScreenState()),
    MovieInteractionListener {

    @AssistedFactory
    interface Factory {
        fun create(movieId: Long): MovieViewModel
    }

    init {
        loadMovieData()
        addMovieToHistory()
    }

    private fun loadMovieData() {
        getBasicDetails()
        getActors()
        getReviews()
        getSimilarMovies()
        getMovieInFavorite()
        getMovieIsRated()
    }

    private fun getMovieIsRated() {
        tryToCall(
            block = { getRatedItemsUseCase.getRatedMovies(1) },
            onSuccess = ::onGetMovieIsRatedSuccess,
            onError = { }
        )
    }

    private fun onGetMovieIsRatedSuccess(ratedItems: List<Pair<Movie, Double>>) {
        val matchedRating = ratedItems
            .firstOrNull { (movie, _) -> movie.id == movieId }
            ?.second

        updateState { state ->
            state.copy(
                isRated = matchedRating != null,
                userRating = matchedRating?.let { (it / 2).toInt() } ?: state.userRating
            )
        }
    }

    private fun addMovieToHistory() {
        tryToCall(
            block = { accountUseCase.addMovieToHistory(movieId) },
            onSuccess = {},
            onError = {}
        )
    }

    private fun getMovieInFavorite() {
        checkUserLoggedIn {
            loadFavoriteMovies()
        }
    }


    private fun loadFavoriteMovies() {
        tryToCall(
            block = { accountUseCase.getFavoriteMovies(1) },
            onSuccess = ::onLoadFavoriteMoviesSuccess,
            onError = {}
        )
    }

    private fun onLoadFavoriteMoviesSuccess(movies: List<Movie>) {
        updateState { it.copy(isFavorite = movies.any { it.id == movieId }) }
    }


    private fun getBasicDetails() {
        tryToCall(
            onStart = ::onGetBasicDetailsStart,
            block = { movieUseCase.getMovieById(movieId) },
            onSuccess = ::onGetBasicDetailsSuccess,
            onError = ::onGetBasicDetailsError,
            dispatcher = Dispatchers.IO
        )
    }

    private fun onGetBasicDetailsStart() {
        updateState { it.copy(basicDetailsSectionState = ScreenStatus.LOADING) }
    }

    private fun onGetBasicDetailsSuccess(movie: Movie) {
        updateState {
            it.copy(
                basicDetailsSectionState = ScreenStatus.SUCCESS,
                movie = movie.toMovieUiState()
            )
        }
    }

    private fun onGetBasicDetailsError(throwable: Throwable) {
        setError(throwable) { copy(basicDetailsSectionState = ScreenStatus.ERROR) }
    }

    private fun getActors() {
        tryToCall(
            onStart = ::onGetActorsStart,
            block = { movieUseCase.getMovieTopCast(movieId) },
            onSuccess = ::setActors,
            onError = ::onGetActorsError,
            dispatcher = Dispatchers.IO
        )
    }

    private fun onGetActorsStart() {
        updateState { it.copy(castSectionState = ScreenStatus.LOADING) }
    }

    private fun onGetActorsError(throwable: Throwable) {
        setError(throwable) { copy(castSectionState = ScreenStatus.ERROR) }
    }

    private fun setActors(actors: List<Artist>) {
        updateState {
            it.copy(
                castSectionState = ScreenStatus.SUCCESS,
                topCast = actors.map { it.toArtistUiState() }
            )
        }
    }

    private fun getReviews() {
        tryToCall(
            onStart = ::onGetReviewsStart,
            block = { movieUseCase.getMovieReviews(movieId) },
            onSuccess = ::setReviews,
            onError = ::onGetReviewsError,
            dispatcher = Dispatchers.IO
        )
    }

    private fun onGetReviewsStart() {
        updateState { it.copy(reviewsSectionState = ScreenStatus.LOADING) }
    }

    private fun setReviews(reviews: List<Review>) {
        updateState {
            it.copy(
                reviewsSectionState = ScreenStatus.SUCCESS,
                reviews = reviews.map { it.toReviewUiState() }
            )
        }
    }

    private fun onGetReviewsError(throwable: Throwable) {
        setError(throwable) { copy(reviewsSectionState = ScreenStatus.ERROR) }
    }

    private fun getSimilarMovies() {
        tryToCall(
            onStart = ::onGetSimilarMoviesStart,
            block = { movieUseCase.getSimilarMovies(movieId) },
            onSuccess = ::setSimilarMovies,
            onError = ::onGetSimilarMoviesError,
            dispatcher = Dispatchers.IO
        )
    }

    private fun onGetSimilarMoviesStart() {
        updateState { it.copy(similarMoviesSectionState = ScreenStatus.LOADING) }
    }

    private fun setSimilarMovies(movies: List<Movie>) {
        updateState {
            it.copy(
                similarMoviesSectionState = ScreenStatus.SUCCESS,
                similarMovies = movies.map { it.toMovieUiState() }
            )
        }
    }

    private fun onGetSimilarMoviesError(throwable: Throwable) {
        setError(throwable) { copy(similarMoviesSectionState = ScreenStatus.ERROR) }
    }

    override fun onBackClick() {
        sendEffect(MovieEffect.NavigateBack)
    }

    override fun onShareClick() {
        updateState { it.copy(isShareBottomSheetOpen = true) }
    }

    override fun onFavoriteClick() {
        checkUserLoggedIn {
            if (screenState.value.isFavorite) {
                removeFromFavorite()
            } else {
                addToFavorite()
            }
        }
    }

    private fun removeFromFavorite() {
        tryToCall(
            block = { accountUseCase.removeMovieFromFavorite(movieId) },
            onSuccess = { onRemoveFromFavoriteSuccess() },
            onError = ::onRemoveFromFavoriteError
        )
    }

    private fun onRemoveFromFavoriteSuccess() {
        showSnackBar(messageId = R.string.movie_favorite_remove_success, isSuccessful = true)
    }

    private fun onRemoveFromFavoriteError(throwable: Throwable) {
        showSnackBar(messageId = R.string.movie_favorite_remove_fail, isSuccessful = false)
    }

    fun showSnackBar(messageId: Int, isSuccessful: Boolean) {
        viewModelScope.launch {
            updateState {
                it.copy(
                    snackMessageId = messageId,
                    isProcessSuccess = isSuccessful,
                    showSnackBar = true
                )
            }
            delay(2000)
            updateState { it.copy(showSnackBar = false) }
        }
    }

    private fun addToFavorite() {
        tryToCall(
            block = { accountUseCase.addMovieToFavorite(movieId) },
            onSuccess = { onAddToFavoriteSuccess() },
            onError = ::onAddToFavoriteError
        )
    }

    private fun onAddToFavoriteSuccess() {
        updateState { it.copy(isFavorite = true) }
        showSnackBar(R.string.movie_favorite_success, true)
    }

    private fun onAddToFavoriteError(throwable: Throwable) {
        showSnackBar(R.string.movie_favorite_fail, false)
    }

    override fun onRateItClick() {
        tryToCall(
            block = loginUseCase::isUserLoggedIn,
            onSuccess = { authed ->
                if (!authed) {
                    updateState { it.copy(isNoAccountBottomSheetOpen = true) }
                } else {
                    if (screenState.value.isRated) {
                        updateState { it.copy(isRatedSuccessBottomSheetOpen = true) }
                    } else {
                        updateState { it.copy(isRateBottomSheetOpen = true) }
                    }
                }
            },
            onError = {}
        )
    }


    override fun onPlayClick() {
        sendEffect(MovieEffect.PlayTrailer)
    }

    override fun onAddToListClick() {
        checkUserLoggedIn { loadMovieLists() }
    }

    private fun loadMovieLists() {
        tryToCall(
            block = { accountUseCase.getMoviesLists(1) },
            onSuccess = ::onLoadMovieListsSuccess,
            onError = {},
            dispatcher = Dispatchers.Main
        )
    }

    private fun onLoadMovieListsSuccess(mediaLists: List<MediaList>) {
        val uiLists = mediaLists.map { list -> list.toUiState() }
        updateState {
            it.copy(isAddToListBottomSheetOpen = true, moviesLists = uiLists)
        }
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
            block = { onClickListBlock(id) },
            onSuccess = ::onClickListSuccess,
            onError = ::onClickListError
        )
    }

    private suspend fun onClickListBlock(listId: Long): Boolean {
        val movies = accountUseCase.getMoviesOfList(listId, 1)
        return if (screenState.value.movie.id in movies.map { it.id }) {
            false
        } else {
            accountUseCase.addMovieToList(listId, screenState.value.movie.id)
            true
        }
    }

    private fun onClickListError(throwable: Throwable) {
        updateState { it.copy(isAddToListBottomSheetOpen = false) }
        showSnackBar(R.string.error_adding_movie_to_list, false)
    }

    private fun onClickListSuccess(isAdded: Boolean) {
        updateState { it.copy(isAddToListBottomSheetOpen = false) }
        showSnackBar(
            messageId = if (isAdded) R.string.added_to_list
            else R.string.movie_already_in_list, isSuccessful = isAdded
        )
    }

    override fun onSubmitCreateListClicked() {
        tryToCall(
            block = ::onSubmitCreateListClickedBlock,
            onSuccess = ::onSubmitCreateListClickedSuccess,
            onError = ::onSubmitCreateListClickedError
        )
    }

    private suspend fun onSubmitCreateListClickedBlock(): List<MediaList> {
        accountUseCase.createList(screenState.value.listName)
        return accountUseCase.getMoviesLists(1)
    }

    private fun onSubmitCreateListClickedSuccess(moviesLists: List<MediaList>) {
        updateState {
            it.copy(
                showCreateListBottomSheet = false,
                isAddToListBottomSheetOpen = true,
                listName = "",
                moviesLists = moviesLists.map { list -> list.toUiState() }
            )
        }
    }

    private fun onSubmitCreateListClickedError(throwable: Throwable) {
        updateState {
            it.copy(
                showCreateListBottomSheet = false,
                isAddToListBottomSheetOpen = true,
                listName = "",
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

    override fun onCopy(messageId: Int, isSuccessful: Boolean) {
        viewModelScope.launch {
            onDismissShareBottomSheet()
            showSnackBar(R.string.copied_to_clip_board_successfully, isSuccessful)
        }
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

    override fun onDismissRateSuccessBottomSheet() {
        updateState { it.copy(isRatedSuccessBottomSheetOpen = false) }
    }

    override fun onRateChange(rate: Int) {
        updateState { it.copy(rate = rate) }
    }

    override fun onSubmitRateClicked(rate: Int) {
        tryToCall(
            onStart = ::onSubmitRateClickedStart,
            block = { movieUseCase.addMovieRating(movieId, rate * 2.toFloat()) },
            onSuccess = { onSubmitRateClickedSuccess(rate) },
            onError = {},
            dispatcher = Dispatchers.IO
        )
    }

    private fun onSubmitRateClickedStart() {
        updateState { it.copy(isRateBottomSheetOpen = false) }
    }

    private fun onSubmitRateClickedSuccess(rate: Int) {
        updateState {
            it.copy(
                isRatedSuccessBottomSheetOpen = true,
                isRated = true,
                userRating = rate
            )
        }
    }

    override fun onNavigateToLogin() {
        sendEffect(MovieEffect.NavigateToLogin)
    }

    override fun onRefresh() {
        loadMovieData()
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

    private fun checkUserLoggedIn(onLoggedIn: () -> Unit) {
        tryToCall(
            block = loginUseCase::isUserLoggedIn,
            onSuccess = { isLoggedIn ->
                if (isLoggedIn) onLoggedIn()
                else updateState { it.copy(isNoAccountBottomSheetOpen = true) }
            },
            onError = {}
        )
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