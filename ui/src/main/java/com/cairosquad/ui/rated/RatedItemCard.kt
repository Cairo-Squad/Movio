package com.cairosquad.ui.rated

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.basic_component.Icon
import com.cairosquad.design_system.basic_component.Text
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.safe_image_viewer.safe_image_viewer.SafeImageViewer
import com.cairosquad.ui.BuildConfig
import com.cairosquad.ui.movio_component.SwipeToDeleteContainer
import com.cairosquad.viewmodel.rated.MyRatingsScreenState
import java.text.DecimalFormat

@Composable
fun RatedItemCard(
    item: MyRatingsScreenState.RatedItemUiState,
    onItemClick: (Long, Boolean) -> Unit,
    onMovieDelete: (Long, Int) -> Unit,
    onSeriesDelete: (Long, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val formattedRating = DecimalFormat("#.#").format(item.rating)
    SwipeToDeleteContainer(
        onDelete = {
            if (item.isMovie) {
                onMovieDelete(item.id, item.userRating)
            } else {
                onSeriesDelete(item.id, item.userRating)
            }
        }
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(100.dp)
                .clickable {
                    onItemClick(item.id, item.isMovie)
                }
        ) {
            SafeImageViewer(
                model = BuildConfig.IMAGE_BASE_URL + (item.posterPath ?: ""),
                contentDescription = item.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxHeight()
                    .width(76.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(start = 8.dp, end = 16.dp)
                    .weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = item.title,
                    style = Theme.textStyle.title.mediumMedium14,
                    color = Theme.color.surfaces.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                //create a row with stars representing the rating
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                ) {
                    repeat(5) { index ->
                        val starIcon =
                            if (index < item.userRating) com.cairosquad.design_system.R.drawable.review_star else com.cairosquad.design_system.R.drawable.star
                        Icon(
                            painter = painterResource(id = starIcon),
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Text(
                        text = formattedRating,
                        style = Theme.textStyle.label.smallRegular12,
                        color = Theme.color.surfaces.onSurface,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }
    }
}
