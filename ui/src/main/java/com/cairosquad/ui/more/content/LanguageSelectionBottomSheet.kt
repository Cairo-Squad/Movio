package com.cairosquad.ui.more.content

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.R
import com.cairosquad.design_system.basic_component.BottomSheet
import com.cairosquad.design_system.basic_component.Button
import com.cairosquad.design_system.basic_component.Text
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.ui.more.composable.BottomSheetItem
import com.cairosquad.viewmodel.more.MoreScreenState

@Composable
fun LanguageSelectionBottomSheet(
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
