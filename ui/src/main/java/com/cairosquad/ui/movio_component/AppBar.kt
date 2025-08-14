package com.cairosquad.ui.movio_component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.R
import com.cairosquad.design_system.basic_component.Icon
import com.cairosquad.design_system.theme.MovioTheme
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.safe_image_viewer.safe_image_viewer.SafeImageViewer
import com.cairosquad.ui.BuildConfig

@Composable
fun AppBar(
    profileImage: String,
    modifier: Modifier = Modifier,
    onClickProfileImage: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                onClick = {},
                enabled = false
            )
            .padding(horizontal = 16.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.size(height = 28.dp, width = 25.dp),
            imageVector = ImageVector.vectorResource(R.drawable.logo),
            contentDescription = stringResource(R.string.movio)
        )
        BasicText(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .weight(1f),
            text = stringResource(R.string.movio),
            style = Theme.textStyle.display.largeBold18.copy(
                brush = Theme.color.gradiant.logo
            )
        )
        if (profileImage.isNotBlank()) {
            SafeImageViewer(
                modifier = Modifier
                    .size(40.dp)
                    .padding(8.dp)
                    .clip(CircleShape)
                    .clickable(onClick = onClickProfileImage),
                model = BuildConfig.IMAGE_BASE_URL + profileImage,
                contentDescription = stringResource(com.cairosquad.ui.R.string.user_profile_image),
                nudeThreshold = 0.0,
                nonNudeThreshold = 0.0,
                loadingPlaceholder = {
                    LoadingMovieImage(
                        modifier = Modifier
                            .size(40.dp)
                            .padding(8.dp)
                            .clip(CircleShape)
                    )
                }
            )
        } else {
            Image(
                modifier = Modifier
                    .size(40.dp)
                    .padding(8.dp)
                    .clip(CircleShape)
                    .clickable(onClick = onClickProfileImage),
                painter = painterResource(
                    if (Theme.isDark) com.cairosquad.ui.R.drawable.profile_place_holder_dark
                    else com.cairosquad.ui.R.drawable.profile_place_holder_light
                ),
                contentDescription = stringResource(com.cairosquad.ui.R.string.user_profile_image)
            )
        }
    }
}

@Preview
@Composable
private fun AppBarPrev() {
    MovioTheme(isDarkTheme = true) {
        AppBar(profileImage = "")
    }
}