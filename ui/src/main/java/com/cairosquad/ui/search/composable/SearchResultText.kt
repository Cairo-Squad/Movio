package com.cairosquad.ui.search.composable

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.R
import com.cairosquad.design_system.text_style.defaultTextStyle
import com.cairosquad.design_system.theme.Theme

@Composable
fun SearchResultText(
    noOfResults: Int
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        BasicText(
            text = stringResource(R.string.search_result),
            style = defaultTextStyle.title.mediumMedium16.copy(
                Theme.color.surfaces.onSurfaceVariant
            )
        )
        Spacer(modifier = Modifier.size(4.dp))
        BasicText(
            text = "(${stringResource(R.string.number_of_items, noOfResults)})",
            style = defaultTextStyle.label.smallRegular14.copy(
                Theme.color.surfaces.onSurfaceVariant
            )
        )
    }
}