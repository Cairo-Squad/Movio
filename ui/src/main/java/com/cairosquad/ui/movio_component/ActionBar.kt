package com.cairosquad.ui.movio_component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.R
import com.cairosquad.design_system.theme.Theme

@Composable
fun ActionBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(
            4.dp,
            alignment = Alignment.CenterHorizontally
        ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.outline_star),
                contentDescription = stringResource(R.string.Rate_it),
                tint = Theme.color.surfaces.onSurfaceContainer,
            )

            Text(
                text = stringResource(R.string.Rate_it),
                color = Theme.color.surfaces.onSurfaceContainer,
                style = Theme.textStyle.label.smallRegular12,
            )
        }

        Icon(
            modifier = Modifier
                .size(56.dp)
                .padding(5.dp)
                .background(
                    brush = Theme.color.gradiant.primaryGradient,
                    shape = CircleShape
                )
                .padding(15.dp),
            imageVector = ImageVector.vectorResource(id = R.drawable.outline_play),
            contentDescription = stringResource(R.string.play),
            tint = Theme.color.brand.onPrimary,
        )

        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.outline_bookmark),
                contentDescription = stringResource(R.string.add_to_list),
                tint = Theme.color.surfaces.onSurfaceContainer,
            )
            Text(
                text = stringResource(R.string.add_to_list),
                color = Theme.color.surfaces.onSurfaceContainer,
                style = Theme.textStyle.label.smallRegular12,
            )
        }
    }
}

@Preview
@Composable
private fun ActionBarPreview() {
    ActionBar()
}

@Preview(name = "ActionBar RTL", locale = "ar")
@Composable
private fun ActionBarRtlPreview() {
    ActionBar()
}
