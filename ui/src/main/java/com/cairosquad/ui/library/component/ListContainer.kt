package com.cairosquad.ui.library.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.R
import com.cairosquad.design_system.basic_component.Icon
import com.cairosquad.design_system.basic_component.Text
import com.cairosquad.design_system.preview.MultiThemePreviews
import com.cairosquad.design_system.theme.MovioTheme
import com.cairosquad.design_system.theme.Theme

@Composable
fun ListContainer(
	numberOfItems: Int,
	listName: String,
	onListClicked: () -> Unit,
	modifier: Modifier = Modifier,
) {
	Column(
		modifier = modifier
			.clip(RoundedCornerShape(8.dp))
			.clickable(onClick = onListClicked),
		verticalArrangement = Arrangement.spacedBy(8.dp)
	) {
		Box(
			modifier = Modifier
				.clip(RoundedCornerShape(8.dp))
				.width(158.dp)
				.height(128.dp)
		) {
			Image(
				modifier = Modifier.fillMaxSize(),
				painter = painterResource(R.drawable.library),
				contentDescription = "Library Image",
				contentScale = ContentScale.Crop
			)
			Row(
				modifier = Modifier
					.background(Theme.color.surfaces.surfaceVariant)
					.align(Alignment.BottomCenter)
					.height(28.dp)
					.fillMaxWidth()
					.padding(horizontal = 8.dp, vertical = 6.dp),
				verticalAlignment = Alignment.CenterVertically,
				horizontalArrangement = Arrangement.SpaceBetween
			) {
				Text(
					modifier = Modifier.weight(1f),
					text = "$numberOfItems Videos",
					style = Theme.textStyle.label.smallRegular12,
					color = Theme.color.surfaces.onSurfaceVariant
				)
				Icon(
					modifier = Modifier
						.size(16.dp),
					imageVector = ImageVector.vectorResource(R.drawable.ic_list),
					contentDescription = "List Icon",
					tint = Theme.color.surfaces.onSurfaceVariant
				)
			}
		}
		Text(
			text = listName,
			style = Theme.textStyle.title.mediumMedium14,
			color = Theme.color.surfaces.onSurface
		)
	}
}

@MultiThemePreviews
@Composable
private fun ListContainerPreview() {
	MovioTheme() {
		Box(
			modifier = Modifier.background(Theme.color.surfaces.surface)
		) {
			ListContainer(
				numberOfItems = 10,
				listName = "Watchlist",
				onListClicked = {}
			)
		}
	}
}