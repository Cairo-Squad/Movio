package com.cairosquad.ui.movio_component.bottom_sheet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.R
import com.cairosquad.design_system.basic_component.BottomSheet
import com.cairosquad.design_system.basic_component.Button
import com.cairosquad.design_system.basic_component.InputField
import com.cairosquad.design_system.basic_component.Text
import com.cairosquad.design_system.theme.Theme

@Composable
fun CreateListBottomSheet(
    value: String,
    isVisible: Boolean,
    isMovie: Boolean,
    onDismiss: () -> Unit,
    onValueChange: (String) -> Unit,
    onSubmit: () -> Unit,
) {

    BottomSheet(
        isVisible = isVisible,
        onDismiss = onDismiss,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 16.dp)
                .navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(bottom = 8.dp),
                text = stringResource(com.cairosquad.ui.R.string.create_list_title),
                style = Theme.textStyle.body.mediumMedium14,
                color = Theme.color.surfaces.onSurface
            )
            Text(
                modifier = Modifier.padding(bottom = 16.dp),
                text = stringResource(
                    com.cairosquad.ui.R.string.create_list_description,
                    if (isMovie)
                        stringResource(com.cairosquad.ui.R.string.movie)
                    else
                        stringResource(R.string.series)
                ),
                style = Theme.textStyle.label.smallRegular12,
                color = Theme.color.surfaces.onSurfaceContainer,
                textAlign = TextAlign.Center
            )
            InputField(
                modifier = Modifier.padding(bottom = 40.dp),
                value = value,
                onValueChange = onValueChange,
                placeholder = stringResource(com.cairosquad.ui.R.string.list_name),
                leadingIcon = R.drawable.ic_list
            )
            Button(
                text = stringResource(com.cairosquad.ui.R.string.create),
                onClick = {
                    onSubmit()
                },
            )
        }
    }
}