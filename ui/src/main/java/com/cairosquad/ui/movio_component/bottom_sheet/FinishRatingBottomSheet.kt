package com.cairosquad.ui.movio_component.bottom_sheet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cairosquad.design_system.basic_component.BottomSheet
import com.cairosquad.design_system.basic_component.Button
import com.cairosquad.design_system.basic_component.Text
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.ui.R
import com.cairosquad.ui.movio_component.RatingBar

@Composable
fun FinishRatingBottomSheet(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    rating: Int,
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
            Text(
                modifier = Modifier.padding(bottom = 8.dp),
                text = "\uD83C\uDF89",
                style = TextStyle(fontSize = 56.sp, color = Color.Black.copy(0.8f))
            )
            Text(
                modifier = Modifier.padding(bottom = 16.dp),
                text = stringResource(R.string.thank_you_for_your_rating),
                style = Theme.textStyle.label.smallRegular14,
                color = Theme.color.surfaces.onSurfaceContainer
            )
            RatingBar(
                modifier = Modifier.padding(bottom = 40.dp),
                rating = rating,
                onRatingChange = {},
                isClickable = false,
                isRateLarge = true
            )
            Button(
                text = stringResource(R.string.submit),
                onClick = onDismiss
            )
        }
    }
}