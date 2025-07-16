package com.cairosquad.ui.movio_component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.R
import com.cairosquad.design_system.theme.MovioTheme
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.safe_image_viewer.safe_image_viewer.SafeImageViewer
import java.util.Locale

/**
 * @param width null if fillMaxWidth, specific dp value otherwise
 */
@Composable
fun MovieCard(
    title: String,
    vote: Float,
    imgUrl: String?,
    width: Dp?,
    aspectRatio: Float,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .then(
                    if (width != null) {
                        Modifier.width(width)
                    } else {
                        Modifier.fillMaxWidth()
                    }
                )
                .aspectRatio(aspectRatio)
        ) {
            if (imgUrl?.isNotEmpty() == true) {
                SafeImageViewer(
                    model = "https://image.tmdb.org/t/p/w500$imgUrl",
                    contentDescription = stringResource(R.string.movie_poster),
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(8.dp)),
                    loadingPlaceholder = {
                        LoadingMovieImage(
                            Modifier
                                .fillMaxSize()
                        )
                    }
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Theme.color.system.defaultImageBackground),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        imageVector = ImageVector.vectorResource(id = R.drawable.image_icon),
                        contentDescription = stringResource(R.string.default_image_icon),
                        tint = Color(0xFFEFF1F5)
                    )
                }
            }

            Row(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .fillMaxWidth()
                    .height(36.dp)
                    .clip(RoundedCornerShape(topEnd = 8.dp, topStart = 8.dp))
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Black.copy(0.74f),
                                Color.Transparent,
                            )
                        )
                    )
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Icon(
                    modifier = Modifier
                        .padding(end = 4.dp),
                    imageVector = ImageVector.vectorResource(id = R.drawable.star),
                    contentDescription = stringResource(R.string.rating_star),
                    tint = Color.Unspecified,
                )

                Text(
                    text = String.format(Locale.getDefault(), "%.1f", vote),
                    color = Theme.color.system.onWarning,
                    style = Theme.textStyle.label.smallRegular12
                )
            }
        }

        Text(
            text = title,
            modifier = Modifier
                .width(width ?: Dp.Unspecified)
                .padding(top = 8.dp),
            style = Theme.textStyle.title.mediumMedium14,
            color = Theme.color.surfaces.onSurface,
            textAlign = TextAlign.Start,
            maxLines = 2,
            minLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MovieCardPreview() {
    MovioTheme {
        MovieCard(
            title = "The Dark Knight",
            vote = 5.0f,
            imgUrl = "https://m.media-amazon.com/images/M/MV5BMTMxNTMwODM0NF5BMl5BanBnXkFtZTcwODAyMTk2Mw@@._V1_QL75_UX380_CR0,0,380,562_.jpg",
            width = 124.dp,
            aspectRatio = 0.775f,
            modifier = Modifier
        )
    }
}