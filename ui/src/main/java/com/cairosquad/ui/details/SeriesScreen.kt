package com.cairosquad.ui.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.basic_component.AppBar
import com.cairosquad.design_system.basic_component.ExpandableText
import com.cairosquad.design_system.theme.MovioTheme
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.safe_image_viewer.safe_image_viewer.SafeImageViewer
import com.cairosquad.ui.R
import com.cairosquad.ui.movio_component.ActionBar
import com.cairosquad.ui.movio_component.ArtistCard
import com.cairosquad.ui.movio_component.InfoChip
import com.cairosquad.ui.movio_component.MovieCard
import com.cairosquad.ui.movio_component.ReviewCard
import com.cairosquad.ui.movio_component.SectionHeader

@Composable
fun SeriesScreen(
    seriesId: Long
) {
//    val navController = LocalNavController.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Theme.color.surfaces.surface)
            .windowInsetsPadding(WindowInsets.navigationBars)
            .verticalScroll(rememberScrollState())
    ) {
        SafeImageViewer(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .blur(16.dp)
                .offset(y = (-20).dp),
            model = "https://image.tmdb.org/t/p/w500/1XS1oqL89opfnbLl8WnZY1O1uJx.jpg",
            contentDescription = "",
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.statusBars)
                .heightIn(max = 10000.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            userScrollEnabled = false
        ) {
            stickyHeader {
                AppBar(
                    modifier = Modifier
                        .fillMaxWidth(),
                    onBackButtonClicked = {},
                    onShareButtonClicked = {},
                    onFavoriteButtonClicked = {}
                )

            }
            item {
                SafeImageViewer(
                    modifier = Modifier
                        .padding(top = 12.dp, bottom = 24.dp)
                        .size(height = 260.dp, width = 200.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    model = "https://image.tmdb.org/t/p/w500/1XS1oqL89opfnbLl8WnZY1O1uJx.jpg",
                    contentDescription = "",
                )
            }
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    BasicText(
                        modifier = Modifier.padding(bottom = 8.dp),
                        text = "Game of Thrones",
                        style = Theme.textStyle.headline.mediumMedium18.copy(
                            color = Theme.color.surfaces.onSurface
                        )
                    )
                    BasicText(
                        modifier = Modifier.padding(bottom = 12.dp),
                        text = "Sci-Fi & Fantasy, Drama, Action & Adventure",
                        style = Theme.textStyle.label.smallRegular14.copy(
                            color = Theme.color.surfaces.onSurfaceVariant
                        )
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        InfoChip(text = "4.5", imgRes = R.drawable.review_star)
                        InfoChip(text = "7 seasons", imgRes = R.drawable.ic_media)
                        InfoChip(
                            text = "4.5",
                            imgRes = com.cairosquad.design_system.R.drawable.date
                        )
                    }
                    ActionBar()
                }
            }
            item {
                ExpandableText(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(top = 16.dp),
                    text = "Pulled to the far side of the galaxy, where the Federation is 75 years away at maximum warp speed, a Starfleet ship must cooperate with Maquis rebels to find a way home.",
                    color = Theme.color.surfaces.onSurface,
                    style = Theme.textStyle.label.smallRegular14,
                    showMoreStyle = Theme.textStyle.label.smallRegular14,
                    showMoreColor = Theme.color.surfaces.onSurfaceVariant,
                    showLessColor = Theme.color.surfaces.onSurfaceVariant,
                )
            }
            item {
                SectionHeader(
                    modifier = Modifier.padding(top = 32.dp, bottom = 12.dp),
                    title = "Top Cast",
                    actionText = stringResource(com.cairosquad.design_system.R.string.see_all),
                    actionIcon = ImageVector.vectorResource(com.cairosquad.design_system.R.drawable.arrow),
                    onActionClick = { /* TODO */ }
                )
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(20) {
                        ArtistCard(
                            name = "Ana de Armas",
                            imgUrl = "https://image.tmdb.org/t/p/w500/vkoSSVrGxFYvtr2uUdz99ENXF1v.jpg"
                        )
                    }
                }
            }
            item {
                SectionHeader(
                    modifier = Modifier.padding(top = 32.dp, bottom = 12.dp),
                    title = "Current Seasons",
                    actionText = stringResource(com.cairosquad.design_system.R.string.see_all),
                    actionIcon = ImageVector.vectorResource(com.cairosquad.design_system.R.drawable.arrow),
                    onActionClick = { /* TODO */ }
                )
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(20) {
                        ArtistCard(
                            name = "Ana de Armas",
                            imgUrl = "https://image.tmdb.org/t/p/w500/vkoSSVrGxFYvtr2uUdz99ENXF1v.jpg"
                        )
                    }
                }
            }
            item {
                SectionHeader(
                    modifier = Modifier.padding(top = 32.dp, bottom = 12.dp),
                    title = "Reviews",
                    actionText = stringResource(com.cairosquad.design_system.R.string.see_all),
                    actionIcon = ImageVector.vectorResource(com.cairosquad.design_system.R.drawable.arrow),
                    onActionClick = { /* TODO */ }
                )
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(20) {
                        ReviewCard(
                            imgUrl = "https://image.tmdb.org/t/p/w500/vkoSSVrGxFYvtr2uUdz99ENXF1v.jpg",
                            movieTitle = "Ana de Armas",
                            rating = "4.5",
                            reviewDate = "June 14, 2025",
                            reviewText = "This isn’t a film, it’s a live action video game with a predictable plot and loads of energetically choreographed CGI to substitute for anything vaguely akin to a story."
                        )
                    }
                }
            }
            item {
                SectionHeader(
                    modifier = Modifier.padding(top = 32.dp, bottom = 12.dp),
                    title = "Similar Series",
                    actionText = stringResource(com.cairosquad.design_system.R.string.see_all),
                    actionIcon = ImageVector.vectorResource(com.cairosquad.design_system.R.drawable.arrow),
                    onActionClick = { /* TODO */ }
                )
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(20) {
                        MovieCard(
                            modifier = Modifier.width(124.dp),
                            imgUrl = "https://image.tmdb.org/t/p/w500/vkoSSVrGxFYvtr2uUdz99ENXF1v.jpg",
                            title = "Ana de Armas",
                            vote = 4.5f,
                            width = 124.dp,
                            aspectRatio = 0.775f
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun SeriesScreenPreview() {
    MovioTheme(isDarkTheme = true) {
        SeriesScreen(123)
    }
}