package com.cairosquad.viewmodel.details.artist

import com.cairosquad.viewmodel.exception.ErrorStatus

data class ArtistScreenState (
    val artist : ArtistUiState= ArtistUiState(),
    val knownForMovies :List<MovieUiState> = emptyList(),
    val knownForSeries :List<SeriesUiState> =emptyList(),
    val screenStatus: ScreenStatus= ScreenStatus.LOADING,
    val errorStatus: ErrorStatus? = null,

    ) {
    data class ArtistUiState(
        val id: Long = 0L,
        val name: String = "",
        val photoPath: String = "",
        val country: String = "",
        val birthDate: String = "",
        val biography: String = "",
        val department: String = ""
    )

    data class MovieUiState(
        val id: Long = 0L,
        val title: String = "",
        val rating: Float = 0f,
        val posterPath: String = ""
    )
    data class SeriesUiState(
        val id: Long=0L,
        val title: String="",
        val rating: Float=0f,
        val posterPath: String=""
    )


    enum class ScreenStatus {
        LOADING,
        SUCCESS,
        FAILED
    }
}

