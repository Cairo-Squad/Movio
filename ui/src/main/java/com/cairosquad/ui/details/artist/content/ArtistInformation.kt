package com.cairosquad.ui.details.artist.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.R
import com.cairosquad.design_system.basic_component.InfoChip
import com.cairosquad.ui.movio_component.LoadingMovieImage
import com.cairosquad.viewmodel.details.artist.ArtistScreenState

@Composable
fun ArtistInformation(state: ArtistScreenState) {
    when (state.screenStatus) {
        ArtistScreenState.ScreenStatus.LOADING -> {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                repeat(2) {
                    LoadingMovieImage(
                        Modifier
                            .padding(top = 16.dp, start = 16.dp)
                            .clip(CircleShape)
                            .size(width = 60.dp, height = 32.dp)
                    )
                }
            }
        }

        ArtistScreenState.ScreenStatus.SUCCESS -> {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(top = 16.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                val lastWord = state.artist.country
                    .trim()
                    .let { full ->
                        val partsByComma = full.split(",")
                        val lastPart = partsByComma.lastOrNull()?.trim().orEmpty()
                        lastPart.split(" ").lastOrNull()?.trim()
                    }
                    ?.takeIf { it.isNotBlank() }
                if (state.artist.birthDate.isNotEmpty())
                    item {
                        InfoChip(
                            text = state.artist.birthDate,
                            imgRes = R.drawable.date,
                        )
                    }
                if (lastWord != null) {
                    item {
                        InfoChip(
                            text = state.artist.country,
                            imgRes = R.drawable.component_1,
                        )
                    }
                }
            }
        }

        ArtistScreenState.ScreenStatus.FAILED -> {}
    }
}