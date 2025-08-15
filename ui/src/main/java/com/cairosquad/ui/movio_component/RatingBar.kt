package com.cairosquad.ui.movio_component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.R
import com.cairosquad.design_system.basic_component.Icon
import com.cairosquad.design_system.theme.Theme

@Composable
fun RatingBar(
    modifier: Modifier = Modifier,
    rating: Int = 0,
    isClickable: Boolean = true,
    isRateLarge: Boolean = false,
    onRatingChange: (Int) -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (rate in 1..5) {
            val iconColor by animateColorAsState(
                targetValue = if (rate <= rating)
                    Theme.color.system.warning
                else
                    Theme.color.surfaces.onSurfaceVariant,
                animationSpec = tween(350)
            )
            Icon(
                modifier = Modifier
                    .size(if (isRateLarge && rate == rating) 48.dp else 28.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .then(
                        if (isClickable) {
                            Modifier.clickable {
                                onRatingChange(rate)
                            }
                        } else {
                            Modifier
                        }
                    ),
                imageVector = ImageVector.vectorResource(R.drawable.review_star),
                contentDescription = "",
                tint = iconColor
            )
        }
    }
}