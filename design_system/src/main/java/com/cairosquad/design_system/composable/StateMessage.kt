package com.cairosquad.design_system.composable

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.R
import com.cairosquad.design_system.preview.MultiThemePreviews
import com.cairosquad.design_system.theme.MovioTheme
import com.cairosquad.design_system.theme.Theme

@Composable
fun StateMessage(
    @DrawableRes imageDrawable: Int,
    @StringRes titleId: Int,
    @StringRes descriptionId: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier
                .size(width = 180.dp, height = 150.dp)
                .padding(bottom = 16.dp),
            painter = painterResource(imageDrawable),
            contentDescription = stringResource(R.string.state_message_image)
        )
        Text(
            modifier = Modifier.padding(bottom = 8.dp),
            text = stringResource(titleId),
            color = Theme.color.surfaces.onSurface,
            style = Theme.textStyle.title.mediumMedium16
        )
        Text(
            text = stringResource(descriptionId),
            color = Theme.color.surfaces.onSurfaceContainer,
            style = Theme.textStyle.label.smallRegular12,
            textAlign = TextAlign.Center
        )
    }
}

@MultiThemePreviews
@Composable
private fun NoResultPreview() {
    MovioTheme {
        Box(
            Modifier.background(Theme.color.surfaces.surface)
        ) {
            StateMessage(
                imageDrawable = R.drawable.no_result,
                titleId = R.string.no_results_found,
                descriptionId = R.string.no_results_found_description
            )
        }
    }
}

@MultiThemePreviews
@Composable
private fun NoInternetPreview() {
    MovioTheme {
        Box(
            Modifier.background(Theme.color.surfaces.surface)
        ) {
            StateMessage(
                imageDrawable = R.drawable.no_internet,
                titleId = R.string.internet_is_not_available,
                descriptionId = R.string.internet_is_not_available_description
            )
        }
    }
}

@MultiThemePreviews
@Composable
private fun EmptyWatchLaterPreview() {
    MovioTheme {
        Box(
            Modifier.background(Theme.color.surfaces.surface)
        ) {
            StateMessage(
                imageDrawable = R.drawable.watch_later_empty,
                titleId = R.string.watch_later_empty,
                descriptionId = R.string.watch_later_empty_description
            )
        }
    }
}

@MultiThemePreviews
@Composable
private fun GuestPreview() {
    MovioTheme {
        Box(
            Modifier.background(Theme.color.surfaces.surface)
        ) {
            StateMessage(
                imageDrawable = R.drawable.logo,
                titleId = R.string.log_in_to_unlock_your_personal_library,
                descriptionId = R.string.access_your_watch_history_favorites_and_watchlist_all_in_one_place
            )
        }
    }
}

@MultiThemePreviews
@Composable
private fun FavoriteListEmptyPreview() {
    MovioTheme {
        Box(
            Modifier.background(Theme.color.surfaces.surface)
        ) {
            StateMessage(
                imageDrawable = R.drawable.favorite_list_empty,
                titleId = R.string.favorites_list_empty,
                descriptionId = R.string.favorite_list_empty_description
            )
        }
    }
}