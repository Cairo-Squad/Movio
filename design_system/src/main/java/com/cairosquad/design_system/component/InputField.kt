package com.cairosquad.design_system.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.R
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
    isSingleLine: Boolean = true,
    isPasswordField: Boolean = false,
    readOnly: Boolean = false,
    @DrawableRes leadingIcon: Int? = null,
    @DrawableRes trailingIcon: Int? = null,
    onTrailingIconClick: () -> Unit = {},
    onFocusChanged: (Boolean) -> Unit = {},
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    var hasFocus by remember { mutableStateOf(false) }
    val isRtl = LocalLayoutDirection.current == LayoutDirection.Rtl
    val hasFocusGradient = listOf(
        Theme.color.brand.onPrimary,
        Theme.color.brand.primary
    )
    val borderColor = if (error.isBlank())
        if (hasFocus) {
            Brush.horizontalGradient(
                if (isRtl) {
                    hasFocusGradient.reversed()
                } else {
                    hasFocusGradient
                }
            )
        } else {
            SolidColor(Color.Transparent)
        }
    else SolidColor(Theme.color.system.errorContainer)

    Column(
        modifier = modifier
    ) {
        BasicTextField(
            value = value,
            readOnly = readOnly,
            onValueChange = onValueChange,
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(Theme.color.surfaces.surfaceContainer)
                .onFocusChanged { focusState ->
                    onFocusChanged(focusState.isFocused)
                    hasFocus = focusState.isFocused
                }
                .then(
                    if (hasFocus || error.isNotBlank()) {
                        Modifier.border(
                            width = 1.dp,
                            brush = borderColor,
                            shape = RoundedCornerShape(8.dp)
                        )
                    } else {
                        Modifier
                    }
                )
                .padding(horizontal = 12.dp, vertical = 14.dp),
            singleLine = isSingleLine,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            textStyle = Theme.textStyle.label.smallRegular14.copy(
                color = Theme.color.surfaces.onSurface
            ),
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextFieldIcon(
                        leadingIcon,
                        hasFocus,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Box(
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        if (value.isEmpty()) {
                            Text(
                                text = placeholder,
                                style = Theme.textStyle.label.smallRegular14.copy(
                                    color = Theme.color.surfaces.onSurfaceContainer
                                )
                            )
                        } else {
                            innerTextField()
                        }
                    }
                    TextFieldIcon(
                        trailingIcon,
                        hasFocus,
                        modifier = Modifier.padding(start = 8.dp),
                        onTrailingIconClick,
                    )
                }
            },
            cursorBrush = SolidColor(
                Theme.color.surfaces.onSurface
            ),
            visualTransformation = if (isPasswordField) PasswordVisualTransformation() else VisualTransformation.None,
        )

        if (error.isNotBlank()) {
            BasicText(
                text = "* $error",
                style = Theme.textStyle.label.smallRegular12.copy(
                    color = Theme.color.system.onErrorContainer
                ),
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
private fun TextFieldIcon(
    @DrawableRes icon: Int?,
    isFocused: Boolean,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    if (icon != null) {
        Icon(
            imageVector = ImageVector.vectorResource(icon),
            contentDescription = null,
            tint = if (isFocused) Theme.color.surfaces.onSurface else Theme.color.surfaces.onSurfaceContainer,
            modifier = modifier
                .clip(CircleShape)
                .size(20.dp)
                .then(
                    if (onClick != null) {
                        Modifier.clickable(onClick = onClick)
                    } else {
                        Modifier
                    }
                )
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
            placeholder = stringResource(R.string.search),
            leadingIcon = R.drawable.search_bottom_nav,
            trailingIcon = R.drawable.ic_close,
            isPasswordField = false,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@MultiThemePreviews
@Composable
private fun PreviewInputFieldError() {
    MovioTheme {
        InputField(
            value = "",
            onValueChange = {},
            placeholder = stringResource(R.string.search),
            leadingIcon = R.drawable.search_bottom_nav,
            trailingIcon = R.drawable.ic_close,
            isPasswordField = false,
            error = "Hello",
            modifier = Modifier.padding(16.dp)
        )
    }
}