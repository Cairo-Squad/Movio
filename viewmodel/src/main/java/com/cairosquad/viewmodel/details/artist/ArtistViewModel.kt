package com.cairosquad.viewmodel.details.artist

import android.util.Log
import com.cairosquad.domain.exception.MovioException
import com.cairosquad.domain.usecase.artists.GetArtistDetailsUseCase
import com.cairosquad.viewmodel.base.BaseViewModel
import com.cairosquad.viewmodel.exception.ErrorStatus
import com.cairosquad.viewmodel.exception.exceptionToErrorStatus

class ArtistViewModel(
    private val getArtistDetailsUseCase: GetArtistDetailsUseCase,
    artistId: Long
) : BaseViewModel<ArtistScreenState, ArtistEffect>(initialState = ArtistScreenState()),
    ArtistInteractionListener {

    init {
        loadArtistDetails(artistId)
        loadArtistMovies(artistId)
        loadArtistSeries(artistId)
    }

    fun loadArtistDetails(artistId: Long) {
        tryToCall(
            block = {
                updateState { it.copy(screenStatus = ArtistScreenState.ScreenStatus.LOADING) }
                getArtistDetailsUseCase.getArtist(artistId).toArtistUiState()
            },
            onSuccess = { artist ->
                updateState {
                    it.copy(artist = artist, screenStatus = ArtistScreenState.ScreenStatus.SUCCESS)
                }
            }, onError = { e ->
                updateState {
                    it.copy(
                        screenStatus = ArtistScreenState.ScreenStatus.FAILED,
                        errorStatus = handleArtistException(e)
                    )
                }
            }
        )
    }

    fun loadArtistMovies(artistId: Long) {
        tryToCall(
            block = {
                getArtistDetailsUseCase.getMoviesOfArtist(artistId)
            },
            onSuccess = { movies ->
                updateState {
                    it.copy(knownForMovies = movies.map { it.toArtistMovieUiState() })
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

    fun loadArtistSeries(artistId: Long) {
        tryToCall(
            block = {
                val series = getArtistDetailsUseCase
                    .getSeriesOfArtist(artistId)
                    .map { it.toArtistSeriesUiState() }
                series
            },
            onSuccess = { series ->
                Log.d("ASDASD", "loadArtistSeries: $series")
                updateState {
                    it.copy(KnownForSeries = series)
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

    override fun onClickBack() {
        sendEffect(ArtistEffect.NavigateBack)
    }

    override fun onMovieClick(movieID: Long) {
        sendEffect(ArtistEffect.NavigateToMovieDetails(movieID))
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


