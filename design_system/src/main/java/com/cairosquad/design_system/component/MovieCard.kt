package com.cairosquad.design_system.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.cairosquad.design_system.R
import com.cairosquad.design_system.theme.MovioTheme
import com.cairosquad.design_system.theme.Theme

@Composable
fun MovieCard(
    title: String,
    vote: Float,
    imgRes: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .widthIn(max = 101.33.dp)
    ) {
        Box(
            modifier = Modifier
        ) {
            AsyncImage(
                model = imgRes,
                contentDescription = stringResource(R.string.movie_poster),
                modifier = Modifier
                    .heightIn(max = 136.dp)
                    .widthIn(max = 101.33.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(5.dp)),
                contentScale = ContentScale.Crop,
                error = painterResource(id = R.drawable.movie_card_img)
            )

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
                    text = vote.toString(),
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
            imgRes = R.drawable.movie_card_img
        )
    }
}

@Preview(name = "MovieCard Light", showBackground = true)
@Composable
private fun MovieCardPreviewLight() {
    MovioTheme(isDarkTheme = false) {
        MovieCard(
            title = "Our girl",
            vote = 4.2f,
            imgRes = R.drawable.girl
        )
    }
}

@Preview(name = "MovieCard Dark", showBackground = true)
@Composable
private fun MovieCardPreviewDark() {
    MovioTheme(isDarkTheme = true) {
        MovieCard(
            title = "Spider-Man: Into the Spider-Verse",
            vote = 4.8f,
            imgRes = R.drawable.spider,
            modifier = Modifier.padding(8.dp)
        )
    }
}

