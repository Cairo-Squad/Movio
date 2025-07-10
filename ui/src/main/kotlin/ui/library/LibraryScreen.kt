package ui.library

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.cairosquad.design_system.theme.Theme

@Composable
fun LibraryScreen() {
    Column(modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        BasicText(
            "Library Screen",
            style = Theme.textStyle.title.mediumMedium16.copy(
                color = Theme.color.surfaces.onSurface
            ),
        )
    }
}