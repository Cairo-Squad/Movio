package com.cairosquad.ui.movio_component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.theme.MovioTheme
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.ui.R

@Composable
fun TrendingMovieCard(imgUrl: String, modifier: Modifier = Modifier) {
    Row(
        modifier = Modifier
            .height(100.dp)
            .fillMaxWidth()
    ) {
        Image(
            modifier = Modifier
                .size(height = 100.dp, width = 76.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Theme.color.system.onError),
            painter = painterResource(com.cairosquad.design_system.R.drawable.popcorn_ic),
            contentDescription = null
        )
        Column(modifier = Modifier.padding(start = 8.dp)) {

        }
    }
}

@Preview
@Composable
fun TrendingMovieCardPrev(modifier: Modifier = Modifier) {
    MovioTheme(isDarkTheme = true) {
        TrendingMovieCard("")
    }
}