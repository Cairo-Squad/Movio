package com.cairosquad.ui.details.composable

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
import com.cairosquad.ui.movio_component.ReviewCard
import com.cairosquad.ui.movio_component.SectionHeader
import com.cairosquad.viewmodel.details.movie.MovieScreenState
import com.cairosquad.viewmodel.details.series.SeriesDetailsScreenState

@Composable
fun SeriesReviewSection(
    reviews: List<SeriesDetailsScreenState.ReviewUiState>,
    onActionClicked: () -> Unit,
) {
    SectionHeader(
        modifier = Modifier.padding(top = 32.dp, bottom = 12.dp),
        title = stringResource(R.string.reviews),
        actionText = stringResource(R.string.see_all),
        actionIcon = ImageVector.vectorResource(R.drawable.arrow),
        onActionClick = onActionClicked
    )
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.Start)
    ) {
        items(reviews) {
            ReviewCard(
                imgUrl = it.authorPhotoPath,
                reviewerName = it.author,
                rating = it.rating.toString(),
                reviewDate = it.date,
                reviewText = it.description,
                isExpandable = false
            )
        }
    }
}

@Composable
fun MovieReviewSection(
    reviews: List<MovieScreenState.ReviewUiState>,
    onActionClicked: () -> Unit,
) {
    SectionHeader(
        modifier = Modifier.padding(top = 32.dp, bottom = 12.dp),
        title = stringResource(R.string.reviews),
        actionText = stringResource(R.string.see_all),
        actionIcon = ImageVector.vectorResource(R.drawable.arrow),
        onActionClick = onActionClicked
    )
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.Start)
    ) {
        items(reviews) {
            ReviewCard(
                imgUrl = it.authorPhotoPath,
                reviewerName = it.author,
                rating = it.rating.toString(),
                reviewDate = it.date,
                reviewText = it.description,
                isExpandable = false
            )
        }
    }
}