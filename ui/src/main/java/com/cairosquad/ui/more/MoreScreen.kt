package com.cairosquad.ui.more

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.cairosquad.design_system.R
import com.cairosquad.design_system.basic_component.BottomSheet
import com.cairosquad.design_system.basic_component.Button
import com.cairosquad.design_system.basic_component.Icon
import com.cairosquad.design_system.basic_component.Text
import com.cairosquad.design_system.theme.MovioTheme
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.safe_image_viewer.safe_image_viewer.SafeImageViewer
import com.cairosquad.ui.BuildConfig
import com.cairosquad.ui.navigation.LocalNavController
import com.cairosquad.ui.navigation.LoginRoute
import com.cairosquad.ui.navigation.MyRatingsRoute
import com.cairosquad.ui.utils.ObserveAsEffect
import com.cairosquad.viewmodel.more.MoreScreenEffect
import com.cairosquad.viewmodel.more.MoreScreenInteractionListener
import com.cairosquad.viewmodel.more.MoreScreenState
import com.cairosquad.viewmodel.more.MoreViewModel

@Composable
fun MoreScreen(
    viewModel: MoreViewModel = hiltViewModel(),

    ) {
    val navController = LocalNavController.current
    val state by viewModel.screenState.collectAsStateWithLifecycle()
    ObserveAsEffect(viewModel.effect) {
        when (it) {
            MoreScreenEffect.NavigateToLogin -> {
                navController.navigate(LoginRoute) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            }

            MoreScreenEffect.NavigateToMyRatings -> {
                navController.navigate(MyRatingsRoute)
            }

        }

    }
    MoreScreenContent(
        state = state,
        listener = viewModel
    )
}

@Composable
fun MoreScreenContent(state: MoreScreenState, listener: MoreScreenInteractionListener) {

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

@Composable
private fun LogoutBottomSheet(
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

@Composable
private fun LanguageSelectionBottomSheet(
    currentLanguage: MoreScreenState.Language,
    isBottomSheetOpen: Boolean,
    onConfirmClicked: (MoreScreenState.Language) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedLanguage by remember { mutableStateOf(currentLanguage) }

    BottomSheet(
        modifier = Modifier,
        isVisible = isBottomSheetOpen,
        onDismiss = { onDismiss() },
        content = {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.choose_language),
                    style = Theme.textStyle.body.mediumMedium14,
                    color = Theme.color.surfaces.onSurface,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .fillMaxWidth()
                )

                val languages = listOf(
                    MoreScreenState.Language(
                        "en",
                        stringResource(com.cairosquad.ui.R.string.english)
                    ),
                    MoreScreenState.Language(
                        "ar",
                        stringResource(com.cairosquad.ui.R.string.arabic)
                    )
                )

                languages.forEach { language ->
                    BottomSheetItem(
                        isSelected = selectedLanguage.code == language.code,
                        text = language.name,
                        onClick = { selectedLanguage = language },
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    text = stringResource(com.cairosquad.ui.R.string.confirm),
                    onClick = { onConfirmClicked(selectedLanguage) },
                    modifier = Modifier
                        .padding(bottom = 32.dp)
                        .navigationBarsPadding()
                )
            }
        }
    )
}


@Composable
fun ThemeSelectionBottomSheet(
    theme: MoreScreenState.Theme,
    isThemeBottomSheetOpen: Boolean,
    onConfirmClicked: (MoreScreenState.Theme) -> Unit,
    onDismiss: () -> Unit,
) {
    var selectedTheme by remember { mutableStateOf(theme) }

    BottomSheet(
        modifier = Modifier,
        isVisible = isThemeBottomSheetOpen,
        onDismiss = { onDismiss() },
        content = {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(com.cairosquad.ui.R.string.choose_theme),
                    style = Theme.textStyle.body.mediumMedium14,
                    color = Theme.color.surfaces.onSurface,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .fillMaxWidth()
                )
                BottomSheetItem(
                    isSelected = selectedTheme == MoreScreenState.Theme.DARK,
                    icon = painterResource(com.cairosquad.ui.R.drawable.moon),
                    text = stringResource(com.cairosquad.ui.R.string.dark_mode),
                    onClick = { selectedTheme = MoreScreenState.Theme.DARK },
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                BottomSheetItem(
                    isSelected = selectedTheme == MoreScreenState.Theme.LIGHT,
                    icon = painterResource(com.cairosquad.ui.R.drawable.sun),
                    text = stringResource(com.cairosquad.ui.R.string.light_mode),
                    onClick = { selectedTheme = MoreScreenState.Theme.LIGHT },
                    modifier = Modifier.padding(bottom = 40.dp)
                )
                Button(
                    modifier = Modifier
                        .padding(bottom = 32.dp)
                        .navigationBarsPadding(),
                    text = stringResource(com.cairosquad.ui.R.string.confirm),
                    onClick = { onConfirmClicked(selectedTheme) },
                )
            }
        }
    )
}


@Composable
fun BottomSheetItem(
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    icon: Painter? = null,
    text: String,
    onClick: () -> Unit,
) {
    val borderColor = if (isSelected) {
        Brush.linearGradient(
            listOf(
                Color(0xFFEBE6FE),
                Color(0xFF724CF8)
            )
        )
    } else {
        Brush.linearGradient(
            listOf(
                Color.Transparent,
                Color.Transparent
            )
        )
    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .border(
                width = 1.dp,
                brush = borderColor,
                shape = RoundedCornerShape(8.dp)
            )
            .background(Theme.color.surfaces.surfaceContainer)
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        icon?.let {
            Icon(
                painter = icon,
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 16.dp)
                    .size(24.dp), tint = Theme.color.surfaces.onSurface
            )
        }

        Text(
            text = text,
            style = Theme.textStyle.title.mediumMedium14,
            color = Theme.color.surfaces.onSurface,
            modifier = Modifier
                .padding(start = 8.dp)
        )
        Spacer(Modifier.weight(1f))
        if (isSelected) {
            Image(
                painter = painterResource(
                    if (Theme.isDark) R.drawable.snack_bar_icon_success_tick_dark
                    else R.drawable.snack_bar_icon_success_tick_light
                ),
                contentDescription = stringResource(com.cairosquad.ui.R.string.success_check_mark),
                modifier = Modifier
                    .padding(end = 12.dp)
                    .size(20.dp)
            )
        }
    }
}

@Preview
@Composable
fun BottomSheetItemPreview() {
    BottomSheetItem(
        isSelected = false,
        icon = painterResource(com.cairosquad.ui.R.drawable.moon),
        text = "Dark Mode",
        onClick = {}
    )
}


@Composable
private fun ShowLoggedOutState(
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
