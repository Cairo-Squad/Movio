package com.cairosquad.ui.movio_component

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.basic_component.Chip
import com.cairosquad.design_system.theme.MovioTheme

@Composable
fun CategoriesChips(
    categories: List<String>,
    selectedChipIndex: Int,
    onChipSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        verticalAlignment = Alignment.CenterVertically
    ) {
        categories.forEachIndexed { index, categoryTitle  ->
            val isSelected = selectedChipIndex == index
            Chip(
                modifier = Modifier.padding(start = 12.dp),
                title = categoryTitle ,
                isSelected = isSelected,
            ) {
                onChipSelected(index)
            }
        }

    }
}

@Preview(locale = "ar")
@Composable
private fun CategoriesChipsPreviewAr() {
    val arabicGenres = listOf(
        "الكل",
        "أكشن",
        "رسوم متحركة",
        "جريمة",
        "رعب"
    )
    MovioTheme(isDarkTheme = true) {
        CategoriesChips(arabicGenres, 0, {})
    }
}
@Preview(locale = "en")
@Composable
private fun CategoriesChipsPreviewEn() {
    val englishGenres = listOf(
        "All",
        "Action",
        "Animation",
        "Crime",
        "Horror"
    )
    MovioTheme(isDarkTheme = true) {
        CategoriesChips(englishGenres, 0, {})
    }
}