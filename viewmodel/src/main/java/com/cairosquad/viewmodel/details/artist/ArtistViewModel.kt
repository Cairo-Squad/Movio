package com.cairosquad.viewmodel.details.artist

import com.cairosquad.domain.exception.MovioException
import com.cairosquad.domain.usecase.ManageArtistUseCase
import com.cairosquad.viewmodel.base.BaseViewModel
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
        fetchArtistDetails(artistId)
        fetchArtistMovies(artistId)
        fetchArtistSeries(artistId)
    }

    fun fetchArtistDetails(artistId: Long) {
        tryToCall(
            onStart = {
                updateState { it.copy(screenStatus = ArtistScreenState.ScreenStatus.LOADING) }
            },
            block = {
                manageArtistUseCase.getArtistById(artistId).toUiState()
            },
            onSuccess = { artist ->
                updateState {
                    it.copy(artist = artist, screenStatus = ArtistScreenState.ScreenStatus.SUCCESS)
                }
            }, onError = { e ->
                updateState {
                    it.copy(
                        screenStatus = ArtistScreenState.ScreenStatus.FAILED,
                        errorStatus = handleArtistException(e),
                        showSnackBar = true,
                        snackMessage = "Failed to load artist",
                        isProcessSuccess = false
                    )
                }
            }
        )
    }

    fun fetchArtistMovies(artistId: Long) {
        tryToCall(
            onStart = {
                updateState { it.copy(screenStatus = ArtistScreenState.ScreenStatus.LOADING) }
            },
            block = {
                manageArtistUseCase.getMoviesOfArtist(artistId)
            },
            onSuccess = { movies ->
                updateState {
                    it.copy(knownForMovies = movies.map { it.toUiState() })
                }
            },
            onError = { e ->
                updateState {
                    it.copy(
                        screenStatus = ArtistScreenState.ScreenStatus.FAILED,
                        errorStatus = handleArtistException(e)
                    )
                }
            }
        )
    }

    fun fetchArtistSeries(artistId: Long) {
        tryToCall(
            onStart = {
                updateState { it.copy(screenStatus = ArtistScreenState.ScreenStatus.LOADING) }
            },
            block = {
                val series = manageArtistUseCase
                    .getSeriesOfArtist(artistId)
                    .map { it.toUiState() }
                series
            },
            onSuccess = { series ->
                updateState {
                    it.copy(knownForSeries = series)
                }
            },
            onError = { e ->
                updateState {
                    it.copy(
                        screenStatus = ArtistScreenState.ScreenStatus.FAILED,
                        errorStatus = handleArtistException(e)
                    )
                }
            }
        )
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
        return when (e) {
            is MovioException -> {
                exceptionToErrorStatus(e)
            }

            else -> ErrorStatus.UNKNOWN_ERROR
        }
    }
}