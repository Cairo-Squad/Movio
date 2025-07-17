package com.cairosquad.ui.details

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.basic_component.AppBar
import com.cairosquad.design_system.theme.MovioTheme
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.safe_image_viewer.safe_image_viewer.SafeImageViewer
import com.cairosquad.ui.R
import com.cairosquad.ui.navigation.LocalNavController
import com.cairosquad.viewmodel.details.episodes.EpisodesDetailsInteractionListener
import com.cairosquad.viewmodel.details.episodes.EpisodesDetailsScreenState
import com.cairosquad.viewmodel.details.episodes.EpisodesDetailsViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cairosquad.ui.utils.ObserveAsEvent
import com.cairosquad.ui.utils.errorStatusToMessageResource
import com.cairosquad.viewmodel.details.episodes.EpisodesDetailEffect
import androidx.compose.runtime.getValue

@Composable
fun EpisodesScreenhost(
     seriesId: Long,
     seasonNumber: Int,
    viewModel: EpisodesDetailsViewModel = koinViewModel { parametersOf(seriesId,seasonNumber) }
){
    val navController = LocalNavController.current
    val context = LocalContext.current
    val uiState by viewModel.screenState.collectAsStateWithLifecycle()

    ObserveAsEvent(viewModel.effect) { effect ->
        when (effect) {
            EpisodesDetailEffect.NavigateBack -> {
                navController.popBackStack()
            }

            is EpisodesDetailEffect.ErrorHappened -> {
                Toast.makeText(
                    context,
                    context.getString(errorStatusToMessageResource(effect.message)),
                    Toast.LENGTH_LONG
                ).show()
            }
            EpisodesDetailEffect.PlayEpisodes -> TODO()
        }
    }
    EpisodesScreen(uiState = uiState, listener = viewModel)
}


@Composable
fun EpisodesScreen(
    uiState: EpisodesDetailsScreenState,
    listener: EpisodesDetailsInteractionListener
) {
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
                .height(375.dp)
                .blur(16.dp)
                .offset(y = (-20).dp),
            model = "https://image.tmdb.org/t/p/w500/${uiState.season.posterUrl}",
            contentDescription = "",
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.statusBars)
                .heightIn(max = 10000.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            userScrollEnabled = false,
        ) {
            stickyHeader {
                AppBar(
                    modifier = Modifier
                        .fillMaxWidth(),
                    onBackButtonClicked = {},
                )
            }
            item {
                SafeImageViewer(
                    modifier = Modifier
                        .padding(top = 12.dp, bottom = 24.dp)
                        .size(height = 260.dp, width = 200.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    model = "https://image.tmdb.org/t/p/w500/${uiState.season.posterUrl}",
                    contentDescription = "",
                )
            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BasicText(
                        modifier = Modifier.padding(bottom = 8.dp),
                        text = "Episodes ${uiState.season.episodesCount}",
                        style = Theme.textStyle.headline.mediumMedium18.copy(
                            color = Theme.color.surfaces.onSurface
                        )
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    SeasonChip(text = "Season ${uiState.season.seasonNumber}", imgRes = R.drawable.drop_down_arrow)
                }
            }
            itemsIndexed(uiState.episodes){ index , episode ->
                 EpisodeCard(
                    episodeNumber = episode.number,
                    episodeImageUrl = "https://image.tmdb.org/t/p/w500/${episode.imageUrl}",
                    episodeName = episode.name,
                    episodeDuration = episode.runtime,
                    episodeRating = episode.rating,
                )
            }
        }
    }
}

@Composable
fun SeasonChip(
    text: String,
    imgRes: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .clip(CircleShape)
            .background(
                color = Theme.color.surfaces.surfaceContainer,
            )
            .border(
                width = 1.dp,
                color = Theme.color.surfaces.onSurfaceAt3,
                shape = CircleShape
            )
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.padding(end = 4.dp, bottom = 1.dp),
            text = text,
            color = Theme.color.surfaces.onSurfaceContainer,
            style = Theme.textStyle.label.smallRegular12,
        )
        Icon(
            imageVector = ImageVector.vectorResource(id = imgRes),
            contentDescription = stringResource(R.string.icon),
            tint = Color.Unspecified
        )
    }
}

@Composable
fun EpisodeCard(
    modifier: Modifier = Modifier,
    episodeName: String,
    episodeNumber: Int,
     episodeDuration: Int,
     episodeRating: Float,
    episodeImageUrl: String? = null
) {
    Row(
        modifier = modifier
            .padding(bottom = 12.dp)
            .padding(horizontal = 16.dp)
            .background(Theme.color.surfaces.surface)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(

        ) {
            SafeImageViewer(
                contentDescription = "Episode Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(width = 100.dp, height = 74.dp)
                    .clip(RoundedCornerShape(8.dp)),
                model = episodeImageUrl.toString()
            )
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.4f))
                    .blur(16.dp)
                    .align(Alignment.Center)
            )
            Image(
                painter = painterResource(R.drawable.outline_play),
                contentDescription = "Play",
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(width = 7.dp, height = 8.dp)
            )
        }
        Column(
            modifier = Modifier.padding(end = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = episodeName,
                    color = Theme.color.surfaces.onSurface,
                    style = Theme.textStyle.title.mediumMedium14,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f)
                )
                Image(
                    painter = painterResource(R.drawable.review_star),
                    contentDescription = "Rating",
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = "$episodeRating",
                    color = Theme.color.surfaces.onSurface,
                    style = Theme.textStyle.label.smallRegular12
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Episode $episodeNumber",
                    color = Theme.color.surfaces.onSurfaceContainer,
                    style = Theme.textStyle.label.smallRegular12
                )
                Text(
                    text = " • ",
                    color = Theme.color.surfaces.onSurfaceContainer,
                    style = Theme.textStyle.label.smallRegular12
                )
                Text(
                    text = "$episodeDuration+m",
                    color = Theme.color.surfaces.onSurfaceContainer,
                    style = Theme.textStyle.label.smallRegular12
                )
            }
        }
    }
}

@Preview()
@Composable
fun PreviewEpisodeCard() {
    MovioTheme(isDarkTheme = true) {
        val episode = EpisodeCard(
            episodeId = 1.toString(),
            episodeNumber = 1,
            episodeImageUrl = "https://image.tmdb.org/t/p/w500/1XS1oqL89opfnbLl8WnZY1O1uJx.jpg",
            episodeName = "Unimatrix Zero",
            episodeDuration = 44,
            episodeRating = 4.5f,
        )
        //EpisodeCard(episode = episode)
    }
}

@Preview
@Composable
private fun SeasonChipPreview() {
    MovioTheme(isDarkTheme = true) {
        SeasonChip(text = "Season 1", imgRes = R.drawable.drop_down_arrow)
    }
}

@Preview(showBackground = true)
@Composable
fun EpisodesScreenPreview() {
    MovioTheme(isDarkTheme = true) {
        //EpisodesScreen(1L, 1)
    }
}

//data class
data class EpisodeCard(
    val episodeId: String,
    val episodeName: String,
    val episodeNumber: Int,
    val episodeDuration: Int,
    val episodeRating: Float,
    val episodeImageUrl: String? = null
)


