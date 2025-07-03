package com.cairosquad.ui.composable

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
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.cairosquad.design_system.theme.MovioTheme
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.ui.R

@Composable
fun MovieCard(
    title: String,
    rating: String,
    imgRes: Int
) {
    Column(
        modifier = Modifier
            .widthIn(max = 102.dp)
    ) {
        Box(
            modifier = Modifier
                .widthIn(max = 101.5.dp)
                .heightIn(max = 178.dp)
        ) {
            AsyncImage(
                model = painterResource(id = imgRes),
                contentDescription = stringResource(R.string.movie_poster),
                modifier = Modifier
                    .heightIn(max = 136.dp)
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
                        .size(16.dp)
                        .padding(end = 4.dp),
                    painter = painterResource(id = R.drawable.star),
                    contentDescription = "Rating Star",
                    tint = Theme.color.system.warning,
                )

                Text(
                    text = rating,
                    fontSize = 10.sp,
                    color = Theme.color.system.warning,
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
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview
@Composable
private fun MovieCardPreview() {
    MovioTheme {
        MovieCard(
            title = "The Dark Knight",
            rating = "5.0",
            imgRes = R.drawable.movie_card_img
        )
    }
}
