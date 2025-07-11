package com.cairosquad.design_system.component

import androidx.annotation.Keep
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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

@Composable
fun MovieCard(
    title: String,
    vote: Float,
    imgUrl: String?,
    cardSize: MovieCardSize,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Box {
            if (imgUrl?.isNotEmpty() == true) {
                SafeImageViewer(
                    model = "https://image.tmdb.org/t/p/w500$imgUrl",
                    contentDescription = stringResource(R.string.movie_poster),
                    modifier = Modifier
                        .height(cardSize.size)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(5.dp)),
                    loadingPlaceholder = {
                        LoadingMovieImage(
                            Modifier
                                .height(cardSize.size)
                                .fillMaxWidth()
                        )
                    }
                )
            } else {
                Image(
                    modifier = Modifier
                        .height(cardSize.size)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(5.dp)),
                    painter = painterResource(com.cairosquad.safe_image_viewer.R.drawable.error),
                    contentDescription = stringResource(R.string.error_loading_image)
                )
            }

            Row(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 8.dp, end = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier
                        .padding(end = 4.dp)
                        .size(16.dp),
                    painter = painterResource(id = R.drawable.star),
                    contentDescription = "Rating Star",
                    tint = Theme.color.system.warning,
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

@Keep
enum class MovieCardSize(val size: Dp) {
    Small(size = 136.dp),
    Medium(size = 160.dp),
    Large(size = 180.dp)
}


@Preview(showBackground = true)
@Composable
private fun MovieCardPreview() {
    MovioTheme {
        MovieCard(
            title = "The Dark Knight",
            vote = 5.0f,
            imgUrl = "https://m.media-amazon.com/images/M/MV5BMTMxNTMwODM0NF5BMl5BanBnXkFtZTcwODAyMTk2Mw@@._V1_QL75_UX380_CR0,0,380,562_.jpg",
            cardSize = MovieCardSize.Large
        )
    }
}