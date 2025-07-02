package com.cairosquad.ui.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.theme.MovioTheme
import com.cairosquad.design_system.theme.Theme

@Composable
fun MovioChip (isSelected: Boolean, modifier: Modifier = Modifier) {
    val backgroundColor = if (isSelected) Theme.color.brand.primary else Theme.color.surfaces.onSurfaceAt3
    val textColor = if (isSelected) Theme.color.brand.onPrimary else Theme.color.surfaces.onSurfaceVariant

    Box(
        modifier = modifier
            .background(color = backgroundColor, shape = RoundedCornerShape(24.dp))
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ){
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = "All",
            color = textColor,
            style = Theme.textStyle.label.smallRegular14,
        )
    }
}

@Preview
@Composable
private fun MovioChipPreview(){
    MovioTheme {
        MovioChip(isSelected = true)
    }
}