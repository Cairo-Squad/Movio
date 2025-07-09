package com.cairosquad.design_system.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.R
import com.cairosquad.design_system.theme.MovioTheme
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.safe_image_viewer.SafeImageViewer

@Composable
fun MovieCard(
    title: String,
    vote: Float,
    imgUrl: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.width(101.33.dp)
    ) {
        Box {
            SafeImageViewer(
                model = imgUrl,
                contentDescription = stringResource(R.string.movie_poster),
                modifier = Modifier
                    .height(136.dp)
                    .width(101.33.dp)
                    .clip(RoundedCornerShape(5.dp)),
                enableLog = true
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
            imgUrl = "https://m.media-amazon.com/images/M/MV5BMTMxNTMwODM0NF5BMl5BanBnXkFtZTcwODAyMTk2Mw@@._V1_QL75_UX380_CR0,0,380,562_.jpg"
        )
    }
}