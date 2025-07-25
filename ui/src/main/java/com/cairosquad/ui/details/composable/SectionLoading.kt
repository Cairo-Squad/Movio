package com.cairosquad.ui.details.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.R
import com.cairosquad.ui.movio_component.SectionHeader

@Composable
fun SectionLoading(
    headerName: String,
    sectionLoadingItem: @Composable () -> Unit
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SectionHeader(
            title = headerName,
            actionText = stringResource(R.string.see_all),
            actionIcon = ImageVector.vectorResource(R.drawable.arrow)
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(10) {
                sectionLoadingItem()
            }
        }
    }
}