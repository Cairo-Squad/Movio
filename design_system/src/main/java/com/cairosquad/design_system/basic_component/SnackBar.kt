package com.cairosquad.design_system.basic_component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.preview.MultiThemePreviews
import com.cairosquad.design_system.theme.MovioTheme
import com.cairosquad.design_system.theme.Theme

@Composable
fun SnackBar(
    imageVector: ImageVector,
    message: String,
    action: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    val gradientBrush = Theme.color.gradiant.primaryGradient
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Theme.color.surfaces.surfaceContainer, RoundedCornerShape(8.dp))
            .padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier
                .graphicsLayer(alpha = 1f)
                .drawWithContent {
                    drawContent()
                    drawRect(
                        brush = gradientBrush,
                        blendMode = BlendMode.Multiply
                    )
                },
            imageVector = imageVector,
            contentDescription = "Snack Bar Icon",
        )
        Text(
            text = message,
            color = Theme.color.surfaces.onSurface,
            style = Theme.textStyle.label.smallRegular14
        )
        action()
    }
}

@MultiThemePreviews
@Composable
private fun SnackBarPreview() {
    MovioTheme {

    }
}