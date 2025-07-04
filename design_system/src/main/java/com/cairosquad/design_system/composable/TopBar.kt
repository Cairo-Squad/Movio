package com.cairosquad.design_system.composable

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
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.theme.MovioTheme
import com.cairosquad.design_system.theme.Theme

@Composable
fun TopBar(
    tabs: List<String>,
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
     Row(
         modifier = modifier
             .fillMaxWidth()
             .height(36.dp)
             .padding(horizontal = 16.dp),
         horizontalArrangement = Arrangement.SpaceAround,
         verticalAlignment = Alignment.CenterVertically
     ) {
         tabs.forEachIndexed { index, title ->
             val isSelected = selectedTabIndex == index
             val textColor = if (isSelected)
                 Theme.color.brand.onPrimaryContainer
             else
                 Theme.color.surfaces.onSurfaceVariant

             val textStyle = if (isSelected)
                 Theme.textStyle.title.mediumMedium16
             else
                 Theme.textStyle.body.smallRegular16
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
                     style = textStyle,
                 )
                 if (isSelected){
                     Box(
                         modifier = Modifier
                             .height(1.dp)
                             .width(with(LocalDensity.current) { tabWidth.toDp() })
                             .background(brush = Theme.color.indicatorGradiant.horizontalGradient)
                     )
                 }
             }

         }
     }
}

@Preview
@Composable
private fun TopBarPreview(){
    MovioTheme {
        val tabs = listOf("All", "Movies", "TV Shows", "Categories")
        var selectedTabIndex by remember { mutableIntStateOf(0) }
        Box(Modifier.background(Theme.color.surfaces.surface)){
            TopBar(
                tabs = tabs,
                selectedTabIndex = selectedTabIndex,
                onTabSelected = {selectedTabIndex = it}
            )
        }
    }
}