package com.cairosquad.ui.movio_component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.R
import com.cairosquad.design_system.basic_component.DsIcon
import com.cairosquad.design_system.basic_component.DsText
import com.cairosquad.design_system.basic_component.ExpandableText
import com.cairosquad.design_system.theme.Theme.color
import com.cairosquad.design_system.theme.Theme.textStyle
import com.cairosquad.safe_image_viewer.safe_image_viewer.SafeImageViewer

@Composable
fun ReviewCard(
    imgUrl: String?,
    reviewerName: String,
    rating: String,
    reviewDate: String,
    reviewText: String,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .border(
                width = 1.dp,
                color = color.surfaces.onSurfaceAt3,
                shape = RoundedCornerShape(8.dp)
            )
            .clip(RoundedCornerShape(8.dp))
            .heightIn(min = 137.dp)
            .width(258.dp)
            .background(color.surfaces.surfaceContainer)
            .padding(12.dp)
    ) {
        Row {
            if (imgUrl?.isNotEmpty() == true) {
                SafeImageViewer(
                    model = "https://image.tmdb.org/t/p/w500$imgUrl",
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .align(Alignment.CenterVertically),
                    contentDescription = stringResource(R.string.reviewer_image),
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .align(Alignment.CenterVertically)
                        .background(color.system.defaultImageBackground),
                    contentAlignment = Alignment.Center
                ) {
                    DsIcon(
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
                DsText(
                    text = reviewerName,
                    color = color.surfaces.onSurface,
                    style = textStyle.title.mediumMedium14
                )
                DsText(
                    modifier = Modifier.padding(top = 4.dp),
                    text = reviewDate,
                    color = color.surfaces.onSurfaceContainer,
                    style = textStyle.body.smallRegular10
                )

            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                DsIcon(
                    imageVector = ImageVector.vectorResource(R.drawable.review_star),
                    contentDescription = stringResource(R.string.rating_star),
                    tint = Color.Unspecified,
                )
                DsText(
                    modifier = Modifier.padding(start = 4.dp),
                    text = rating,
                    color = color.system.onWarning,
                    style = textStyle.label.smallRegular12
                )
            }
        }
        ExpandableText(
            text = reviewText,
            color = color.surfaces.onSurfaceVariant,
            style = textStyle.label.smallRegular12,
            collapsedMaxLine = 4,
            showMoreText = "... " + stringResource(R.string.more),
            showMoreStyle = textStyle.label.mediumMedium12,
            showMoreColor = color.brand.onPrimaryContainer,
            showLessText = " " + stringResource(R.string.less),
            showLessStyle = textStyle.label.mediumMedium12,
            showLessColor = color.brand.onPrimaryContainer,
            modifier = Modifier.padding(top = 12.dp)
        )

    }
}
