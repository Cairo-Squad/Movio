package com.cairosquad.ui.more.content

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.R
import com.cairosquad.design_system.basic_component.Text
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.safe_image_viewer.safe_image_viewer.SafeImageViewer
import com.cairosquad.ui.BuildConfig
import com.cairosquad.ui.more.composable.SettingsItem
import com.cairosquad.ui.more.composable.ShowLoggedOutState
import com.cairosquad.viewmodel.more.MoreScreenInteractionListener
import com.cairosquad.viewmodel.more.MoreScreenState

@Composable
fun MoreScreenContent(
    state: MoreScreenState,
    listener: MoreScreenInteractionListener
) {

    val animatedBlur by animateDpAsState(
        if (
            state.isThemeBottomSheetOpen
            || state.isLanguageBottomSheetOpen
            || state.isLogoutButtonVisible
        ) 4.dp else 0.dp
    )

    if (!state.isUserLoggedIn) {
        ShowLoggedOutState(
            onClick = { listener.onLoginClick() }
        )
    } else {
        Box {
            Image(
                painter = painterResource(
                    if (Theme.isDark) com.cairosquad.ui.R.drawable.more_background_dark
                    else com.cairosquad.ui.R.drawable.more_background_light
                ),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .blur(animatedBlur)
            )
            Column(
                modifier = Modifier
                    .blur(animatedBlur)
                    .statusBarsPadding()
                    .verticalScroll(rememberScrollState())
                    .fillMaxWidth()
                    .padding(top = 104.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (state.userProfileImage != null) {
                    SafeImageViewer(
                        modifier = Modifier
                            .padding(bottom = 8.dp)
                            .clip(CircleShape)
                            .size(100.dp),
                        nudeThreshold = 0.0,
                        nonNudeThreshold = 0.0,
                        model = state.userProfileImage
                            ?.takeIf { it.isNotBlank() }
                            ?.let { BuildConfig.IMAGE_BASE_URL + it }.toString(),
                        placeholder = painterResource(
                            if (Theme.isDark) com.cairosquad.ui.R.drawable.more_background_dark
                            else com.cairosquad.ui.R.drawable.more_background_light
                        ),
                        contentDescription = stringResource(com.cairosquad.ui.R.string.profile_image),
                    )
                } else {
                    Image(
                        painter = painterResource(
                            if (Theme.isDark) com.cairosquad.ui.R.drawable.profile_place_holder_dark
                            else com.cairosquad.ui.R.drawable.profile_place_holder_light
                        ),
                        contentDescription = stringResource(com.cairosquad.ui.R.string.profile_image),
                        modifier = Modifier
                            .padding(bottom = 8.dp)
                            .size(100.dp)
                            .clip(CircleShape)
                    )
                }
                Text(
                    text = state.name.takeIf { it.isNotBlank() } ?: state.username,
                    style = Theme.textStyle.title.mediumMedium14,
                    color = Theme.color.surfaces.onSurface,
                    modifier = Modifier.padding(bottom = 24.dp)
                )
                SettingsItem(
                    icon = painterResource(id = com.cairosquad.ui.R.drawable.star_unfilled),
                    title = stringResource(com.cairosquad.ui.R.string.my_ratings),
                    trailingIcon = painterResource(id = R.drawable.arrow),
                    modifier = Modifier.padding(bottom = 12.dp),
                    onClick = listener::onMyRatingsClick
                )
                SettingsItem(
                    icon = painterResource(id = com.cairosquad.ui.R.drawable.palette),
                    title = stringResource(com.cairosquad.ui.R.string.theme),
                    trailingText = stringResource(state.currentTheme.stringResId),
                    trailingIcon = painterResource(id = R.drawable.arrow),
                    modifier = Modifier.padding(bottom = 12.dp),
                    onClick = listener::onThemeClick
                )
                SettingsItem(
                    icon = painterResource(id = com.cairosquad.ui.R.drawable.earth),
                    title = stringResource(com.cairosquad.ui.R.string.language),
                    trailingText = state.currentLanguage.name,
                    trailingIcon = painterResource(id = R.drawable.arrow),
                    modifier = Modifier.padding(bottom = 12.dp),
                    onClick = listener::onLanguageClick
                )
                SettingsItem(
                    icon = painterResource(id = com.cairosquad.ui.R.drawable.phone),
                    title = stringResource(com.cairosquad.ui.R.string.app_version),
                    trailingText = "v" + state.appVersion,
                    modifier = Modifier.padding(bottom = 12.dp),
                    onClick = {}
                )
                SettingsItem(
                    icon = painterResource(id = com.cairosquad.ui.R.drawable.logout),
                    title = stringResource(com.cairosquad.ui.R.string.logout),
                    trailingIcon = painterResource(id = R.drawable.arrow),
                    modifier = Modifier.padding(bottom = 12.dp),
                    onClick = listener::onLogoutClick
                )
            }
        }
        ThemeSelectionBottomSheet(
            theme = state.currentTheme,
            isThemeBottomSheetOpen = state.isThemeBottomSheetOpen,
            onConfirmClicked = listener::onThemeBottomSheetConfirm,
            onDismiss = listener::onThemeBottomSheetDismiss
        )
        LanguageSelectionBottomSheet(
            currentLanguage = state.currentLanguage,
            isBottomSheetOpen = state.isLanguageBottomSheetOpen,
            onConfirmClicked = listener::onLanguageBottomSheetConfirm,
            onDismiss = listener::onLanguageBottomSheetDismiss
        )
        LogoutBottomSheet(
            isVisible = state.isLogoutButtonVisible,
            onConfirmClicked = listener::onLogoutConfirm,
            onDismiss = listener::onLogoutDismiss
        )
    }

}