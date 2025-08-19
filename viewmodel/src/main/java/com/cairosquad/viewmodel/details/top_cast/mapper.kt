package com.cairosquad.viewmodel.details.top_cast

import com.cairosquad.entity.Artist
import com.cairosquad.viewmodel.details.top_cast.TopCastScreenState.ArtistUiState

fun Artist.toUiState(): ArtistUiState {
    return ArtistUiState(
        id = id,
        name = name,
        photoPath = photoPath
    )
}