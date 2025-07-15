package com.cairosquad.ui.movio_component

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.preview.MultiThemePreviews
import com.cairosquad.design_system.theme.MovioTheme
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.ui.R

@Composable
fun ReviewCard(
    reviewerImage: Painter,
    movieTitle: String,
    rating: String,
    reviewDate: String,
    reviewText: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .border(
                width = 1.dp,
                color = Theme.color.surfaces.onSurfaceAt3,
                shape = RoundedCornerShape(8.dp)
            )
            .clip(RoundedCornerShape(8.dp))
            .height(137.dp)
            .width(258.dp)
            .background(Theme.color.surfaces.surfaceContainer)
            .padding(12.dp)
    ) {
        Row(
        ) {
            Image(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .align(Alignment.CenterVertically),
                painter = reviewerImage,
                contentDescription = stringResource(R.string.reviewer_image)
            )
            Column(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .weight(1f)
            ) {
                Text(
                    text = movieTitle,
                    color = Theme.color.surfaces.onSurface,
                    style = Theme.textStyle.title.mediumMedium14
                )
                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    text = reviewDate,
                    color = Theme.color.surfaces.onSurfaceContainer,
                    style = Theme.textStyle.body.smallRegular10
                )

            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.review_star),
                    contentDescription = stringResource(R.string.rating_star),
                    tint = Color.Unspecified,
                )
                Text(
                    modifier = Modifier.padding(start = 4.dp),
                    text = rating,
                    color = Theme.color.system.onWarning,
                    style = Theme.textStyle.label.smallRegular12
                )
            }
        }
        Text(
            modifier = Modifier.padding(top = 12.dp),
            text = reviewText,
            color = Theme.color.surfaces.onSurfaceVariant,
            style = Theme.textStyle.label.smallRegular12,
            maxLines = 4,
            overflow = TextOverflow.Ellipsis
        )

    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Composable
private fun ReviewCardLightPreview() {
    MovioTheme (isDarkTheme = false){
        ReviewCard(
            reviewerImage = painterResource(com.cairosquad.design_system.R.drawable.more_bottom_nav_colored),
            movieTitle = "Awkwafina",
            rating = "4.5",
            reviewDate = "June 14, 2025",
            reviewText = "This isn’t a film, it’s a live action video game with a predictable plot and loads of energetically choreographed CGI to substitute for anything vaguely akin to a story."
        )
    }
}
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Composable
private fun ReviewCardNightPreview() {
    MovioTheme (isDarkTheme = true){
        ReviewCard(
            reviewerImage = painterResource(com.cairosquad.design_system.R.drawable.more_bottom_nav_colored),
            movieTitle = "Awkwafina",
            rating = "4.5",
            reviewDate = "June 14, 2025",
            reviewText = "This isn’t a film, it’s a live action video game with a predictable plot and loads of energetically choreographed CGI to substitute for anything vaguely akin to a story."
        )
    }
}