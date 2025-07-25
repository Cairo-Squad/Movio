package com.cairosquad.design_system.basic_component

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
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
    isErrorMessageShown: Boolean = true,
    isSingleLine: Boolean = true,
    isPasswordField: Boolean = false,
    readOnly: Boolean = false,
    maxCharacters: Int? = 100,
    @DrawableRes leadingIcon: Int? = null,
    @DrawableRes trailingIcon: Int? = null,
    onTrailingIconClick: () -> Unit = {},
    onFocusChanged: (Boolean) -> Unit = {},
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    var hasFocus by rememberSaveable { mutableStateOf(false) }
    var textFieldValue by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(text = value, selection = TextRange(value.length)))
    }
    val isRtl = LocalLayoutDirection.current == LayoutDirection.Rtl

    val hasFocusGradientColors = listOf(
        Theme.color.brand.onPrimary,
        Theme.color.brand.primary
    )
    val hasFocusGradient = Brush.horizontalGradient(
        if (isRtl) {
            hasFocusGradientColors.reversed()
        } else {
            hasFocusGradientColors
        }
    )
    val noErrorBorder = if (hasFocus) {
        hasFocusGradient
    } else {
        SolidColor(Color.Transparent)
    }

    val errorGradientColors = listOf(
        Theme.color.system.onError,
        Theme.color.system.errorContainer
    )
    val errorBorder = Brush.horizontalGradient(
        if (isRtl) {
            errorGradientColors.reversed()
        } else {
            errorGradientColors
        }
    )

    val borderColor = if (error.isBlank()) {
        noErrorBorder
    } else {
        errorBorder
    }

    Column(
        modifier = modifier
    ) {
        BasicTextField(
            value = textFieldValue,
            readOnly = readOnly,
            onValueChange = { newValue ->
                val filteredValue = if (maxCharacters != null && newValue.text.length > maxCharacters) {
                    val truncatedText = newValue.text.take(maxCharacters)
                    newValue.copy(
                        text = truncatedText,
                        selection = TextRange(truncatedText.length.coerceAtMost(newValue.selection.start))
                    )
                } else {
                    newValue
                }

                textFieldValue = filteredValue
                onValueChange(filteredValue.text)
            },
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
                        error = error.isNotBlank(),
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Box(
                        modifier = Modifier.weight(1f)
                    ) {
                        innerTextField()

                        if (textFieldValue.text.isEmpty()) {
                            Text(
                                text = placeholder,
                                style = Theme.textStyle.label.smallRegular14.copy(
                                    color = Theme.color.surfaces.onSurfaceContainer
                                )
                            )
                        }
                    }
                    TextFieldIcon(
                        trailingIcon,
                        hasFocus,
                        modifier = Modifier.padding(start = 8.dp),
                        error = error.isNotBlank(),
                        onTrailingIconClick,
                    )
                }
            },
            cursorBrush = SolidColor(
                Theme.color.surfaces.onSurface
            ),
            visualTransformation = if (isPasswordField) PasswordVisualTransformation() else VisualTransformation.None,
        )

        // عرض عداد الحروف (اختياري)
        if (maxCharacters != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = "${textFieldValue.text.length}/$maxCharacters",
                    style = Theme.textStyle.label.smallRegular12,
                    color = if (textFieldValue.text.length >= maxCharacters) {
                        Theme.color.system.errorContainer
                    } else {
                        Theme.color.surfaces.onSurfaceContainer
                    }
                )
            }
        }

        AnimatedVisibility(
            error.isNotBlank() && isErrorMessageShown,
            modifier = Modifier.padding(top = 12.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.info_circle),
                    contentDescription = stringResource(R.string.icon),
                    tint = Theme.color.system.errorContainer,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = error,
                    style = Theme.textStyle.label.smallRegular12,
                    color = Theme.color.system.errorContainer,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2
                )
            }
        }
    }
}
@Composable
private fun TextFieldIcon(
    @DrawableRes icon: Int?,
    isFocused: Boolean,
    modifier: Modifier = Modifier,
    error: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    val tintColor = if (error) {
        Theme.color.system.onError
    } else {
        if (isFocused) Theme.color.surfaces.onSurface else Theme.color.surfaces.onSurfaceContainer
    }

    if (icon != null) {
        AnimatedContent(
            targetState = icon,
            transitionSpec = {
                scaleIn(animationSpec = tween(300)).togetherWith(scaleOut(animationSpec = tween(300)))
            }
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(it),
                contentDescription = null,
                tint = tintColor,
                modifier = modifier
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