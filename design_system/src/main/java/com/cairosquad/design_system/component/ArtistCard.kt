package com.cairosquad.design_system.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.R
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.safe_image_viewer.safe_image_viewer.SafeImageViewer

@Composable
fun ArtistCard(name: String, imgUrl: String?, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .widthIn(max = 102.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (imgUrl?.isNotEmpty() == true) {
            SafeImageViewer(
                model = "https://image.tmdb.org/t/p/w500$imgUrl",
                modifier = Modifier
                    .padding(horizontal = 6.67.dp)
                    .size(88.dp)
                    .clip(CircleShape),
                contentDescription = stringResource(R.string.artist_image),
            )
        } else {
            Image(
                modifier = Modifier
                    .padding(horizontal = 6.67.dp)
                    .size(88.dp)
                    .clip(CircleShape),
                painter = painterResource(com.cairosquad.safe_image_viewer.R.drawable.empty_profile_image),
                contentDescription = stringResource(R.string.error_loading_image)
            )
        }
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            text = name,
            textAlign = TextAlign.Center,
            style = Theme.textStyle.title.mediumMedium14,
            color = Theme.color.surfaces.onSurface,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )
    }
}
