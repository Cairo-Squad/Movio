package com.cairosquad.viewmodel.details.series

import com.cairosquad.viewmodel.exception.ErrorStatus

data class SeriesDetailsScreenState(
    val basicDetailsSectionState: SectionStatus = SectionStatus.LOADING,
    val castSectionState: SectionStatus = SectionStatus.LOADING,
    val seasonsSectionState: SectionStatus = SectionStatus.LOADING,
    val reviewsSectionState: SectionStatus = SectionStatus.LOADING,
    val similarSeriesSectionState: SectionStatus = SectionStatus.LOADING,

    val errorStatus: ErrorStatus? = null,

    val showShareBottomSheet: Boolean = false,
    val showRateBottomSheet: Boolean = false,
    val showAddToListBottomSheet: Boolean = false,
    val showLoginBottomSheet: Boolean = false,
    val showCreateListBottomSheet: Boolean = false,

    val rating: Int = 0,
    val listName: String = "",

    val showSnackBar: Boolean = false,
    val snackMessage: String = "",
    val isProcessSuccess: Boolean = false,

    val series: SeriesUiState = SeriesUiState(),
    val cast: List<ArtistUiState> = emptyList(),
    val seasons: List<SeasonUiState> = emptyList(),
    val reviews: List<ReviewUiState> = emptyList(),
    val similarSeries: List<SeriesUiState> = emptyList()
) {
    data class SeriesUiState(
        val id: Long = 0L,
        val title: String = "",
        val rating: Float = 0F,
        val posterPath: String = "",
        val genres: List<String> = emptyList(),
        val seasonsCount: Int = 0,
        val releaseDate: String = "",
        val overview: String = "",
        val trailerPath: String = ""
    )

    data class ArtistUiState(
        val id: Long = 0L,
        val name: String = "",
        val photoPath: String = ""
    )

    data class SeasonUiState(
        val number: Int = 0,
        val name: String = "",
        val episodesCount: Int = 0,
        val rating: Float = 0F,
        val posterPath: String = "",
        val overview: String = "",
        val airDate: String = ""
    )

    data class ReviewUiState(
        val id: String = "",
        val author: String = "",
        val authorPhotoPath: String = "",
        val rating: Float = 0F,
        val date: String = "",
        val description: String = "",
    )

    enum class SectionStatus {
        LOADING,
        SUCCESS,
        ERROR
    }
}
