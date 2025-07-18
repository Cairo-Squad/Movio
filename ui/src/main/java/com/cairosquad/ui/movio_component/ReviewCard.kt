package com.cairosquad.ui.movio_component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.R
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.safe_image_viewer.safe_image_viewer.SafeImageViewer

@Composable
fun ReviewCard(
    imgUrl: String,
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
        Row {
            if (imgUrl.isNotEmpty()) {
                SafeImageViewer(
                    model = "https://image.tmdb.org/t/p/w500$imgUrl",
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .align(Alignment.CenterVertically),
                    contentDescription = stringResource(R.string.reviewer_image),
                    nudeThreshold = 0.0,
                    nonNudeThreshold = 0.0
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .align(Alignment.CenterVertically)
                        .background(Theme.color.system.defaultImageBackground),
                    contentAlignment = Alignment.Center
                ){
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.image_icon),
                        contentDescription = stringResource(R.string.reviewer_image),
                        tint = Color(0xFFEFF1F5)
                    )
                }
            }
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