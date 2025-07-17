package com.cairosquad.viewmodel.details.artist

import com.cairosquad.domain.search.exception.MovioException
import com.cairosquad.domain.search.usecase.GetArtistDetailsUseCase
import com.cairosquad.viewmodel.base.BaseViewModel
import com.cairosquad.viewmodel.exception.ErrorStatus
import com.cairosquad.viewmodel.exception.exceptionToErrorStatus

class ArtistViewModel(
    private val getArtistDetailsUseCase: GetArtistDetailsUseCase


) : BaseViewModel<ArtistScreenState, ArtistEffect>(initialState = ArtistScreenState()),
    ArtistInteractionListener {

    fun loadArtistDetails(artistID: Long) {
        tryToCall(

            block = {
                updateState {
                    it.copy(
                        screenStatus = ArtistScreenState.ScreenStatus.LOADING,
                    )
                }
                val artist = getArtistDetailsUseCase.getArtist(artistID).toArtistUiState()
                artist
            },
            onSuccess = {artist->
                updateState {
                    it.copy(artist=artist)
                }
            }
            , onError = {e->
                updateState {
                    it.copy(
                        screenStatus = ArtistScreenState.ScreenStatus.FAILED,
                        errorStatus = handleArtistException(e)
                    )
                }


            }


        )
    }
    fun loadArtistMovies(artistID: Long) {
        tryToCall(
            block = {
               val movies = getArtistDetailsUseCase
                    .getMoviesOfArtist(artistID)
                    .map{it.toArtistMovieUiState() }
                movies
            },
            onSuccess = { movies ->
                updateState {
                    it.copy(knownForMovies = movies)
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
    fun loadArtistSeries(artistID: Long) {
        tryToCall(
            block = {
                val series =getArtistDetailsUseCase
                    .getSeriesOfArtist(artistID)
                    .map{it.toArtistSeriesUiState() }
                series

            },
            onSuccess = { series ->
                updateState {
                    it.copy(KnownForSeries= series)
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

    override fun onMovieClick(movieID : Long) {
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


