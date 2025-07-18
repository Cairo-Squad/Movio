package com.cairosquad.viewmodel.details.top_cast

data class TopCastScreenState(
    val isLoading: Boolean = true,
    val cast: List<ArtistUiState> = emptyList(),
    val error: String? = null
) {
    data class ArtistUiState(
        val id: Long,
        val name: String,
        val photoPath: String
    )
}