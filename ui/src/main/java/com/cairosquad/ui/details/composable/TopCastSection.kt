package com.cairosquad.ui.details.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.R
import com.cairosquad.ui.BuildConfig
import com.cairosquad.ui.movio_component.SectionHeader
import com.cairosquad.ui.movio_component.SmallArtistCard
import com.cairosquad.viewmodel.details.movie.MovieScreenState
import com.cairosquad.viewmodel.details.series.SeriesDetailsScreenState

@Composable
fun SeriesTopCastSection(
    onActionClicked: () -> Unit,
    onArtistClicked: (Long) -> Unit,
    cast: List<SeriesDetailsScreenState.ArtistUiState>
) {
    SectionHeader(
        modifier = Modifier.padding(top = 32.dp, bottom = 12.dp),
        title = stringResource(R.string.top_cast),
        actionText = stringResource(R.string.see_all),
        actionIcon = ImageVector.vectorResource(R.drawable.arrow),
        onActionClick = onActionClicked
    )
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.Start)
    ) {
        items(cast) {
            SmallArtistCard(
                modifier = Modifier.clickable { onArtistClicked(it.id) },
                name = it.name,
                imgUrl = BuildConfig.IMAGE_BASE_URL + it.photoPath
            )
        }
    }
}

@Composable
fun MovieTopCastSection(
    onActionClicked: () -> Unit,
    onArtistClicked: (Long) -> Unit,
    cast: List<MovieScreenState.TopCastUiState>
) {
    SectionHeader(
        modifier = Modifier.padding(top = 32.dp, bottom = 12.dp),
        title = stringResource(R.string.top_cast),
        actionText = stringResource(R.string.see_all),
        actionIcon = ImageVector.vectorResource(R.drawable.arrow),
        onActionClick = onActionClicked

    )
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.Start)
    ) {
        items(cast) {
            SmallArtistCard(
                modifier = Modifier.clickable { onArtistClicked(it.id) },
                name = it.name,
                imgUrl = BuildConfig.IMAGE_BASE_URL + it.photoPath
            )
        }
    }
}