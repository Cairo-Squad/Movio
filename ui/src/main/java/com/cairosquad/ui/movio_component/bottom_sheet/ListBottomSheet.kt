package com.cairosquad.ui.movio_component.bottom_sheet

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.R
import com.cairosquad.design_system.basic_component.BottomSheet
import com.cairosquad.design_system.basic_component.Icon
import com.cairosquad.design_system.basic_component.Text
import com.cairosquad.design_system.theme.Theme

@Composable
fun ListBottomSheet(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    lists: List<String>,
    onListClicked: (Int) -> Unit,
    onCreateNewList: () -> Unit
) {
    BottomSheet(
        isVisible = isVisible,
        onDismiss = onDismiss
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 32.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .clickable(onClick = onCreateNewList),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    modifier = Modifier
                        .background(Theme.color.surfaces.surfaceContainer, shape = CircleShape)
                        .size(32.dp)
                        .padding(6.dp),
                    imageVector = ImageVector.vectorResource(R.drawable.ic_add),
                    contentDescription = "",
                    tint = Theme.color.surfaces.onSurface
                )
                Text(
                    text = stringResource(com.cairosquad.ui.R.string.create_list_title),
                    style = Theme.textStyle.title.mediumMedium14,
                    color = Theme.color.surfaces.onSurface,
                )
            }
            if (lists.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Theme.color.surfaces.onSurfaceAt3)
                )
                lists.forEachIndexed { index, listName ->
                    ListItem(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable(onClick = { onListClicked(index) }),
                        listName = listName
                    )
                }
            }
        }
    }
}

@Composable
private fun ListItem(
    listName: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            text = listName,
            style = Theme.textStyle.title.mediumMedium14,
            color = Theme.color.surfaces.onSurface,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )
        Icon(
            modifier = Modifier
                .size(40.dp)
                .padding(8.dp),
            imageVector = ImageVector.vectorResource(R.drawable.ic_rounded_add),
            contentDescription = stringResource(com.cairosquad.ui.R.string.add_to_list_icon),
            tint = Theme.color.surfaces.onSurfaceContainer
        )
    }
}