package com.cairosquad.ui.library

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.R
import com.cairosquad.design_system.basic_component.AppBar
import com.cairosquad.design_system.theme.MovioTheme
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.ui.library.component.EmptyListContainer
import com.cairosquad.ui.library.component.SectionHeader

@Composable
fun LibraryScreen() {
	LibraryScreenContent()
}

@Composable
private fun LibraryScreenContent() {
	LazyColumn(
		modifier = Modifier
			.fillMaxSize()
			.windowInsetsPadding(WindowInsets.statusBars)
			.background(Theme.color.surfaces.surface)
	) {
		stickyHeader {
			AppBar(
				title = "Library"
			)
		}
		item {
			SectionHeader(
				modifier = Modifier.padding(top = 16.dp, bottom = 12.dp),
				sectionTitle = "Watchlist",
				sectionIcon = ImageVector.vectorResource(R.drawable.ic_list),
				onSectionClick = {}
			)
		}
		item {
			LazyRow(
				contentPadding = PaddingValues(horizontal = 16.dp)
			) {
				item {
					EmptyListContainer { }
				}
			}
		}
		item {
			SectionHeader(
				modifier = Modifier.padding(top = 24.dp),
				sectionTitle = "Watchlist",
				sectionDescription = "This list has empty",
				sectionIcon = ImageVector.vectorResource(R.drawable.heart_icon_round),
				onSectionClick = {}
			)
		}
		item {
			LazyRow(
				contentPadding = PaddingValues(horizontal = 16.dp)
			) {

			}
		}
		item {
			SectionHeader(
				modifier = Modifier.padding(top = 24.dp),
				sectionTitle = "Watchlist",
				sectionDescription = "This list has empty",
				sectionIcon = ImageVector.vectorResource(R.drawable.recent),
				onSectionClick = {}
			)
		}
		item {
			LazyRow(
				contentPadding = PaddingValues(horizontal = 16.dp)
			) {

			}
		}
	}
}


@Preview
@Composable
private fun LibraryScreenPreview() {
	MovioTheme(isDarkTheme = true) {
		LibraryScreenContent()
	}
}