package com.cairosquad.ui.more.content

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.basic_component.BottomSheet
import com.cairosquad.design_system.basic_component.Button
import com.cairosquad.design_system.basic_component.Text
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.ui.more.composable.BottomSheetItem
import com.cairosquad.viewmodel.more.MoreScreenState

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