package com.cairosquad.ui.library.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.R
import com.cairosquad.design_system.basic_component.Icon
import com.cairosquad.design_system.theme.Theme

@Composable
fun EmptyListContainer(
	onClick: () -> Unit
) {
	Column(
		modifier = Modifier
			.clip(RoundedCornerShape(8.dp))
			.clickable(onClick = onClick),
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.spacedBy(2.dp)
	) {
		Box(
			Modifier
				.size(height = 5.dp, width = 142.dp)
				.background(
					Theme.color.surfaces.onSurfaceAt3,
					shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
				)
		)
		Box(
			Modifier
				.size(height = 121.dp, width = 158.dp)
				.background(
					Theme.color.surfaces.surfaceContainer,
					shape = RoundedCornerShape(8.dp)
				),
			contentAlignment = Alignment.Center
		) {
			Icon(
				modifier = Modifier.size(40.dp),
				imageVector = ImageVector.vectorResource(R.drawable.ic_rounded_add),
				contentDescription = "Create new list icon",
				tint = Theme.color.surfaces.onSurfaceVariant
			)
		}
	}
}

@Preview
@Composable
private fun EmptyListContainerPreview(modifier: Modifier = Modifier) {
	EmptyListContainer {  }
}