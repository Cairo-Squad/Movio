package com.cairosquad.design_system.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.preview.MultiThemePreviews
import com.cairosquad.design_system.theme.MovioTheme
import com.cairosquad.design_system.theme.Theme

@Composable
fun InputField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    error: String = "",
    enabled: Boolean = true,
    singleLine: Boolean = true,
    isPassword: Boolean = false,
    @DrawableRes leadingIcon: Int? = null,
    @DrawableRes trailingIcon: Int? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    var isFocused by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Theme.color.surfaces.surface)
    ) { // TODO: Remove Column
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier
                .padding(20.dp) // TODO: Remove
                .clip(RoundedCornerShape(8.dp))
                .background(Theme.color.surfaces.surfaceContainer)
                .onFocusChanged { focusState ->
                    isFocused = focusState.isFocused
                }
                .then(
                    if (isFocused) {
                        Modifier.border(
                            width = 1.dp,
                            brush = Brush.horizontalGradient(
                                listOf(
                                    Color(0xFFEBE6FE),
                                    Color(0xFF724CF8)
                                )
                            ),
                            shape = RoundedCornerShape(8.dp)
                        )
                    } else {
                        Modifier
                    }
                )
                .padding(horizontal = 12.dp, vertical = 14.dp),
            singleLine = singleLine,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            textStyle = Theme.textStyle.label.smallRegular14.copy(
                color = if (isFocused) Theme.color.surfaces.onSurface else Theme.color.surfaces.onSurfaceContainer
            ),

        )
    }
}

@Composable
fun TextFieldIcon(
    @DrawableRes icon: Int?,
    modifier: Modifier = Modifier
) {
    if (icon != null) {
        Icon(
            imageVector = ImageVector.vectorResource(icon),
            contentDescription = null,
            modifier = modifier
        )
    }
}

@MultiThemePreviews
@Composable
private fun PreviewInputField() {
    MovioTheme {
        InputField(
            value = "",
            onValueChange = {},
        )
    }
}