package com.cairosquad.ui.more.content

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.R
import com.cairosquad.design_system.basic_component.BottomSheet
import com.cairosquad.design_system.basic_component.Button
import com.cairosquad.design_system.basic_component.Text
import com.cairosquad.design_system.theme.Theme

@Composable
fun LogoutBottomSheet(
    isVisible: Boolean,
    onConfirmClicked: () -> Unit,
    onDismiss: () -> Unit
) {
    BottomSheet(
        modifier = Modifier,
        isVisible = isVisible,
        onDismiss = { onDismiss() },
        content = {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = stringResource(com.cairosquad.ui.R.string.movio_logo),
                    modifier = Modifier
                        .width(60.dp)
                        .height(66.dp)
                        .padding(bottom = 16.dp)
                )
                Text(
                    modifier = Modifier.padding(bottom = 8.dp),
                    text = stringResource(com.cairosquad.ui.R.string.confirm_logout),
                    style = Theme.textStyle.title.mediumMedium16,
                    color = Theme.color.surfaces.onSurface,
                )
                Text(
                    text = stringResource(com.cairosquad.ui.R.string.you_ll_lose_access_to_your_library_favorites_and_history_until_you_sign_back_in),
                    modifier = Modifier.padding(bottom = 40.dp),
                    style = Theme.textStyle.label.smallRegular12,
                    color = Theme.color.surfaces.onSurfaceContainer,
                    textAlign = TextAlign.Center,
                )
                Button(
                    modifier = Modifier
                        .padding(bottom = 32.dp)
                        .navigationBarsPadding(),
                    text = stringResource(com.cairosquad.ui.R.string.logout),
                    onClick = { onConfirmClicked() },
                )
            }
        }
    )
}