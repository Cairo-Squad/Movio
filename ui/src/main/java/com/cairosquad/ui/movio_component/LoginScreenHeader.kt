package com.cairosquad.ui.movio_component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.basic_component.Icon
import com.cairosquad.design_system.basic_component.Text
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.ui.R


@Composable
 fun LoginScreenHeader(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            painter = painterResource(R.drawable.logo_icon_with_ring),
            contentDescription = stringResource(R.string.icon),
            modifier = Modifier.size(64.dp)
        )
        Text(
            text = stringResource(R.string.welcome_back),
            style = Theme.textStyle.display.mediumMedium20,
            color = Theme.color.surfaces.onSurface
        )

    }
}