package com.cairosquad.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.theme.MovioTheme
import com.cairosquad.design_system.theme.Theme

@Composable
fun MovioTopBar(
    items: List<String>,
    selectedItemIndex: Int,
    onTabSelected: (Int) -> Unit,
) {
     Row(
         modifier = Modifier
             .fillMaxWidth()
             .padding(horizontal = 16.dp, vertical = 8.dp),
         horizontalArrangement = Arrangement.SpaceAround
     ) {
         items.forEachIndexed { index, title ->
             val isSelected = selectedItemIndex == index
             val textColor = if (isSelected)
                 Theme.color.brand.onPrimaryContainer
             else
                 Theme.color.surfaces.onSurfaceVariant
             var tabWidth by remember { mutableIntStateOf(0) }

             Column(
                 modifier = Modifier
                     .clickable(
                     indication = null,
                     interactionSource = remember { MutableInteractionSource() },
                     onClick = {onTabSelected(index)}
                 ),
                 horizontalAlignment = Alignment.CenterHorizontally,
             ) {
                 Text(
                     modifier = Modifier
                         .widthIn(min = 56.dp, max = 100.dp)
                         .onGloballyPositioned { coordinates ->
                             tabWidth = coordinates.size.width
                         },
                     text = title,
                     color = textColor,
                     textAlign = TextAlign.Center,
                     style = Theme.textStyle.title.mediumMedium16,
                 )
                 if (isSelected){
                     Box(
                         modifier = Modifier
                             .height(1.dp)
                             .width(with(LocalDensity.current) { tabWidth.toDp() })
                             .background(
                                 brush = Brush.horizontalGradient(
                                     colors = listOf(
                                         Theme.color.indicatorGradiant.start,
                                         Theme.color.indicatorGradiant.medium,
                                         Theme.color.indicatorGradiant.end
                                     ),
                                 )
                             )
                     )
                 }
             }

         }
     }
}

@Preview
@Composable
private fun MovioTopBarPreview(){
    MovioTheme {
        val items = listOf("All", "Movies", "TV Shows", "Categories")
        var selectedTabIndex by remember { mutableIntStateOf(0) }
        MovioTopBar(
            items = items,
            selectedItemIndex = selectedTabIndex,
            onTabSelected = {selectedTabIndex = it}
        )
    }
}