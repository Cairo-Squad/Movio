package com.cairosquad.ui.auth.content

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.basic_component.Button
import com.cairosquad.design_system.basic_component.Icon
import com.cairosquad.design_system.basic_component.InputField
import com.cairosquad.design_system.basic_component.Text
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.ui.R
import com.cairosquad.ui.movio_component.LoginScreenHeader
import com.cairosquad.ui.utils.errorStatusToMessageResource
import com.cairosquad.ui.utils.validationErrorToStringResource
import com.cairosquad.viewmodel.login.LoginInteractionListener
import com.cairosquad.viewmodel.login.LoginScreenState

@Composable
fun LoginScreenContent(
    uiState: LoginScreenState,
    interactionListener: LoginInteractionListener,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .systemBarsPadding()
            .fillMaxSize()
            .background(color = Theme.color.surfaces.surface)
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {
        LoginScreenHeader(
            Modifier
                .fillMaxWidth()
                .padding(top = 74.dp, bottom = 48.dp)
        )

        val usernameError = uiState.errors[LoginScreenState.FormField.USERNAME]

        InputField(
            value = uiState.username,
            onValueChange = { interactionListener.onUsernameChange(it) },
            placeholder = stringResource(R.string.user_name),
            error = if (usernameError != null) stringResource(
                validationErrorToStringResource(
                    usernameError,
                    LoginScreenState.FormField.USERNAME
                )
            ) else "",
            isErrorMessageShown = true,
            isPasswordField = false,
            leadingIcon = R.drawable.profile_login,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.padding(bottom = 12.dp)
        )

        val passwordError = uiState.errors[LoginScreenState.FormField.PASSWORD]?.let {
            validationErrorToStringResource(it, LoginScreenState.FormField.PASSWORD)
        } ?: uiState.error?.let {
            errorStatusToMessageResource(it)
        }

        InputField(
            value = uiState.password,
            onValueChange = { interactionListener.onPasswordChange(it) },
            placeholder = stringResource(R.string.password),
            error = if (passwordError != null) stringResource(passwordError) else "",
            isErrorMessageShown = false,
            isPasswordField = !uiState.isPasswordVisible,
            leadingIcon = R.drawable.lock,
            trailingIcon = if (uiState.isPasswordVisible) R.drawable.eye else R.drawable.close_eye,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            onTrailingIconClick = { interactionListener.onPasswordVisibilityIconClick() },
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(bottom = 24.dp, top = 12.dp)
                .fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(end = 8.dp)
                    .weight(1f)
            ) {
                AnimatedVisibility(
                    visible = passwordError != null,
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.info_circle),
                            contentDescription = stringResource(R.string.icon),
                            tint = Theme.color.system.errorContainer,
                            modifier = Modifier.size(16.dp)
                        )
                        passwordError?.let { error ->
                            Text(
                                text = stringResource(error),
                                style = Theme.textStyle.label.smallRegular12,
                                color = Theme.color.system.errorContainer,
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 2
                            )
                        }
                    }
                }
            }

            Text(
                text = stringResource(R.string.forgot_password),
                modifier = Modifier
                    .clickable { interactionListener.onForgetPasswordClick() },
                style = Theme.textStyle.label.mediumMedium12,
                color = Theme.color.surfaces.onSurfaceVariant,
                textAlign = TextAlign.End
            )
        }

        Button(
            text = stringResource(R.string.login),
            onClick = { interactionListener.onLoginClick() },
            modifier = Modifier.padding(bottom = 40.dp),
            isLoading = uiState.isLoading
        )

        Row(
            modifier = Modifier.padding(bottom = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(1.dp)
                    .background(color = Theme.color.surfaces.onSurfaceAt3)
            )
            Text(
                text = stringResource(R.string.or),
                color = Theme.color.surfaces.onSurfaceVariant,
                style = Theme.textStyle.label.smallRegular12
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(1.dp)
                    .background(color = Theme.color.surfaces.onSurfaceAt3)
            )
        }
        Button(
            text = stringResource(R.string.continue_as_a_guest),
            onClick = { interactionListener.onContinueAsAGuestClick() },
            modifier = Modifier,
            textStyle = Theme.textStyle.label.mediumMedium14,
            textColor = Theme.color.surfaces.onSurface,
            containerColor = Theme.color.surfaces.surface,
            borderColor = Theme.color.surfaces.onSurfaceAt3
        )
        Spacer(modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier
                .wrapContentWidth()
                .padding(bottom = 32.dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = stringResource(R.string.dont_have_an_account),
                color = Theme.color.surfaces.onSurfaceVariant,
                style = Theme.textStyle.label.smallRegular14
            )
            Text(
                text = stringResource(R.string.signup),
                color = Theme.color.brand.primary,
                style = Theme.textStyle.label.mediumMedium14,
                modifier = Modifier.clickable { interactionListener.onSignUpClick() }
            )
        }

    }
}