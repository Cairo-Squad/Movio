package com.cairosquad.design_system.preview

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

@Preview(
    name = "Dark Mode Arabic",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    locale = "ar",
    showBackground = true
)
@Preview(
    name = "Light Mode Arabic",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    locale = "ar",
    showBackground = true
)
@Preview(
    name = "Dark Mode English",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    locale = "en",
    showBackground = true
)
@Preview(
    name = "Light Mode English",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    locale = "en",
    showBackground = true
)
annotation class MultiThemePreviews