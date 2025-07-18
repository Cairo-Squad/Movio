package com.cairosquad.design_system.basic_component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.R
import com.cairosquad.design_system.theme.MovioTheme
import com.cairosquad.design_system.theme.Theme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDropDownMenu(
    selectedText: String,
    options: List<String>,
    imgRes: Int,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .menuAnchor()
                .clip(CircleShape)
                .background(Theme.color.surfaces.surfaceContainer)
                .border(
                    width = 1.dp,
                    color = Theme.color.surfaces.onSurfaceAt3,
                    shape = CircleShape
                )
                .clickable { expanded = true }
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicText(
                modifier = Modifier.padding(end = 4.dp, bottom = 1.dp),
                text = selectedText,
                style = Theme.textStyle.label.smallRegular12.copy(
                    color = Theme.color.surfaces.onSurfaceContainer
                )
            )

            Icon(
                imageVector = ImageVector.vectorResource(id = imgRes),
                contentDescription = stringResource(R.string.icon),
                tint = Color.Unspecified
            )
        }

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(Theme.color.surfaces.surfaceContainer)
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = option,
                            style = Theme.textStyle.label.smallRegular12.copy(
                                color = Theme.color.surfaces.onSurfaceContainer
                            )
                        )
                    },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppDropDownMenuPreview() {
    var selectedOption by remember { mutableStateOf("Option 1") }
    val options = listOf("Option 1", "Option 2", "Option 3")

    MovioTheme(isDarkTheme = true) {
        AppDropDownMenu(
            selectedText = selectedOption,
            options = options,
            imgRes = R.drawable.drop_down_arrow,
            onOptionSelected = { selectedOption = it },
            modifier = Modifier.padding(16.dp)
        )
    }
}
