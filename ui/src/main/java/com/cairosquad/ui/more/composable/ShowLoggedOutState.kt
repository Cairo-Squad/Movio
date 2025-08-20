package com.cairosquad.ui.more.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.R
import com.cairosquad.design_system.basic_component.Button
import com.cairosquad.design_system.basic_component.Text
import com.cairosquad.design_system.theme.MovioTheme
import com.cairosquad.design_system.theme.Theme

@Composable
fun ShowLoggedOutState(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = stringResource(R.string.logo),
                modifier = Modifier
                    .size(
                        height = 88.dp,
                        width = 80.dp
                    )
                    .padding(
                        bottom = 16.dp
                    )
            )
            Text(
                text = stringResource(com.cairosquad.ui.R.string.log_in_to_unlock_your_personal_library),
                style = Theme.textStyle.title.mediumMedium16,
                color = Theme.color.surfaces.onSurface,
                modifier = Modifier.padding(
                    bottom = 8.dp
                )
            )
            Text(
                text = stringResource(com.cairosquad.ui.R.string.access_your_ratings_and_have_the_ability_to_change_the_theme_and_language),
                style = Theme.textStyle.label.smallRegular12,
                color = Theme.color.surfaces.onSurfaceContainer,
                modifier = Modifier.padding(
                    bottom = 8.dp
                ),
                textAlign = TextAlign.Center
            )
        }

        Button(
            text = stringResource(R.string.login),
            onClick = { onClick() },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 16.dp)
                .padding(bottom = 32.dp)
        )
    }

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ShowLoggedOutStatePreview() {
    MovioTheme {
        ShowLoggedOutState(onClick = {})

    }
}
