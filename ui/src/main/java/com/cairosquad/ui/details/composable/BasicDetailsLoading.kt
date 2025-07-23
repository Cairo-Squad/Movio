package com.cairosquad.ui.details.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.cairosquad.ui.movio_component.LoadingMovieImage

@Composable
fun BasicDetailsLoading() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        LoadingMovieImage(
            modifier = Modifier
                .size(height = 40.dp, width = 48.dp)
                .padding(bottom = 8.dp)
        )
        LoadingMovieImage(
            modifier = Modifier
                .size(height = 40.dp, width = 96.dp)
                .padding(bottom = 12.dp)
        )
        Row(
            modifier = Modifier.padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            repeat(3) {
                LoadingMovieImage(
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(height = 32.dp, width = 62.dp)
                        .padding(bottom = 8.dp)
                )
            }
        }
        Row(
            modifier = Modifier.padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            repeat(3) {
                LoadingMovieImage(
                    modifier = Modifier
                        .height(56.dp)
                        .weight(1f)
                        .padding(bottom = 8.dp)
                )
            }
        }
    }
}