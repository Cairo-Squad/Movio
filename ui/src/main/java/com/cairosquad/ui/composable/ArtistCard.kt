package com.cairosquad.ui.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.text_style.TitleTextStyle
import com.cairosquad.design_system.theme.MovioTheme
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.ui.R

@Composable
fun ArtistCard(name: String = "unknown", imgRes: Int = R.drawable.search_bottom_nav_colored) {
    Column(
        modifier = Modifier
            .widthIn(max = 102.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier
                .size(88.dp)
                .clip(CircleShape)
                .padding(horizontal = 6.dp),
            painter = painterResource(id = imgRes),
            contentDescription = null,
            contentScale = ContentScale.Crop,
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            text = name,
            textAlign = TextAlign.Center,
            style = Theme.textStyle.title.mediumMedium14,

            )

    }
}


@Preview
@Composable
private fun ArtistCardPreview() {
    MovioTheme {
        ArtistCard()
    }
}