package com.cairosquad.ui.movio_component.bottom_sheet

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.basic_component.BottomSheet
import com.cairosquad.design_system.basic_component.Button
import com.cairosquad.design_system.basic_component.Text
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.safe_image_viewer.safe_image_viewer.SafeImageViewer
import com.cairosquad.ui.movio_component.RatingBar

@Composable
fun RateBottomSheet(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    imageUrl: String,
    name: String,
    isMovie: Boolean,
    rating: Int,
    onRatingChange: (Int) -> Unit,
    onSubmitClicked: (Int) -> Unit,
) {
    BottomSheet(
        isVisible = isVisible,
        onDismiss = onDismiss
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SafeImageViewer(
                modifier = Modifier
                    .size(88.dp)
                    .clip(CircleShape)
                    .border(
                        width = 1.dp,
                        color = Theme.color.surfaces.onSurfaceAt2,
                        shape = CircleShape
                    ),
                model = imageUrl,
                contentDescription = "Poster Image"
            )
            Text(
                modifier = Modifier.padding(top = 8.dp, bottom = 24.dp),
                text = name,
                style = Theme.textStyle.title.mediumMedium16,
                color = Theme.color.surfaces.onSurface
            )
            Text(
                modifier = Modifier.padding(bottom = 16.dp),
                text = "Add your overall rating for this ${if (isMovie) "movie" else "series"}",
                style = Theme.textStyle.label.smallRegular14,
                color = Theme.color.surfaces.onSurfaceContainer
            )
            RatingBar(
                modifier = Modifier.padding(bottom = 40.dp),
                rating = rating,
                onRatingChange = onRatingChange,
            )
            Button(
                text = "Submit",
                onClick = { onSubmitClicked(rating) }
            )
        }
    }
}