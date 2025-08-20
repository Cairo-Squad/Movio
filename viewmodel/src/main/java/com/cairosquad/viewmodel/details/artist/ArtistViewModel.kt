package com.cairosquad.viewmodel.details.artist

import com.cairosquad.domain.exception.MovioException
import com.cairosquad.domain.usecase.ManageArtistUseCase
import com.cairosquad.entity.Movie
import com.cairosquad.viewmodel.base.BaseViewModel
import com.cairosquad.viewmodel.details.artist.ArtistScreenState.ArtistUiState
import com.cairosquad.viewmodel.details.artist.ArtistScreenState.SeriesUiState
import com.cairosquad.viewmodel.exception.ErrorStatus
import com.cairosquad.viewmodel.exception.exceptionToErrorStatus
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel(assistedFactory = ArtistViewModel.Factory::class)
class ArtistViewModel @AssistedInject constructor(
    private val manageArtistUseCase: ManageArtistUseCase,
    @Assisted private val artistId: Long,
) : BaseViewModel<ArtistScreenState, ArtistEffect>(initialState = ArtistScreenState()),
    ArtistInteractionListener {

    @AssistedFactory
    interface Factory {
        fun create(artistId: Long): ArtistViewModel
    }

    init {
        initScreen()
    }

    private fun initScreen() {
        fetchArtistDetails(artistId)
        fetchArtistMovies(artistId)
        fetchArtistSeries(artistId)
    }

    fun fetchArtistDetails(artistId: Long) {
        tryToCall(
            onStart = ::setLoading,
            block = { manageArtistUseCase.getArtistById(artistId).toUiState() },
            onSuccess = ::handleArtistDetailsSuccess,
            onError = ::handleArtistDetailsError
        )
    }

    fun fetchArtistMovies(artistId: Long) {
        tryToCall(
            onStart = ::setLoading,
            block = { manageArtistUseCase.getMoviesOfArtist(artistId) },
            onSuccess = ::handleArtistMoviesSuccess,
            onError = ::handleDefaultError
        )
    }

    fun fetchArtistSeries(artistId: Long) {
        tryToCall(
            onStart = ::setLoading,
            block = { manageArtistUseCase.getSeriesOfArtist(artistId).map { it.toUiState() } },
            onSuccess = ::handleArtistSeriesSuccess,
            onError = ::handleDefaultError
        )
    }

    private fun setLoading() {
        updateState { it.copy(screenStatus = ArtistScreenState.ScreenStatus.LOADING) }
    }

    private fun handleArtistDetailsSuccess(artist: ArtistUiState) {
        updateState { it.copy(artist = artist, screenStatus = ArtistScreenState.ScreenStatus.SUCCESS)} }

    private fun handleArtistMoviesSuccess(movies: List<Movie>) {
        updateState { it.copy(knownForMovies = movies.map { m -> m.toUiState() }) }
    }

    private fun handleArtistSeriesSuccess(series: List<SeriesUiState>) {
        updateState { it.copy(knownForSeries = series) }
    }

    private fun handleArtistDetailsError(e: Throwable) {
        updateState {
            it.copy(
                screenStatus = ArtistScreenState.ScreenStatus.FAILED,
                errorStatus = handleArtistException(e),
                showSnackBar = true,
                snackMessage = FAILED_TO_LOAD_ARTIST_MESSAGE,
                isProcessSuccess = false
            )
        }
    }

    private fun handleDefaultError(e: Throwable) {
        updateState {
            it.copy(screenStatus = ArtistScreenState.ScreenStatus.FAILED, errorStatus = handleArtistException(e))
        }
    }

    override fun onBackClick() {
        sendEffect(ArtistEffect.NavigateBack)
    }

    override fun onMovieClick(movieId: Long) {
        sendEffect(ArtistEffect.NavigateToMovieDetails(movieId))
    }

    override fun onSeriesClick(seriesId: Long) {
        sendEffect(ArtistEffect.NavigateToSeriesDetails(seriesId))
    }

    override fun onRefresh() {
        fetchArtistDetails(artistId)
        fetchArtistMovies(artistId)
        fetchArtistSeries(artistId)
    }

    private fun handleArtistException(e: Throwable): ErrorStatus {
        return if (e is MovioException) exceptionToErrorStatus(e)
        else ErrorStatus.UNKNOWN_ERROR
    }

     companion object {
         private const val FAILED_TO_LOAD_ARTIST_MESSAGE = "Failed to load artist"
    }
}