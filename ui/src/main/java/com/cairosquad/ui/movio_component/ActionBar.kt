package com.cairosquad.ui.movio_component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.ui.R

@Composable
fun ActionBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.padding(vertical = 6.5.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.outline_star),
                contentDescription = stringResource(R.string.rate),
                tint = Color.Unspecified,
            )

            Text(
                text = stringResource(R.string.rate),
                color = Theme.color.surfaces.onSurfaceContainer,
                style = Theme.textStyle.label.smallRegular12,

            )
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(horizontal = 8.67.dp)
                .size(46.67.dp)
                .background(
                    brush = Theme.color.gradiant.primaryGradient,
                    shape = CircleShape
                )
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.outline_play),
                contentDescription = stringResource(R.string.play),
                tint = Theme.color.brand.onPrimary,
            )
        }

        Column(
            modifier = Modifier.padding(vertical = 6.5.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.outline_bookmark),
                contentDescription = stringResource(R.string.add_to_list),
                tint = Color.Unspecified,
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