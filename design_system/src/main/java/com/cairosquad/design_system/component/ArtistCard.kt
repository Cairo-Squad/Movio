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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.R
import com.cairosquad.design_system.theme.MovioTheme
import com.cairosquad.design_system.theme.Theme

@Composable
fun ArtistCard(name: String, imgRes: Int, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .widthIn(max = 102.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier
                .padding(horizontal = 6.dp)
                .size(88.dp)
                .clip(CircleShape),
            painter = painterResource(id = imgRes),
            contentDescription = stringResource(R.string.artist_image),
            contentScale = ContentScale.Crop,
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            text = name,
            textAlign = TextAlign.Center,
            style = Theme.textStyle.title.mediumMedium14,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )
    }
}


@Preview
@Composable
private fun ArtistCardPreview() {
    MovioTheme {
        ArtistCard(name = "Ana de Armas", imgRes = R.drawable.artist)
    }
}
