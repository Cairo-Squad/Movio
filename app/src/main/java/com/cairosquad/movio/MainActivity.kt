package com.cairosquad.movio

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.cairosquad.design_system.component.InputField
import com.cairosquad.design_system.theme.MovioTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContent {
            var text by remember { mutableStateOf("") }
            MovioTheme {
                InputField(
                    value = text,
                    onValueChange = { text = it },
                    placeholder = "Search",
                    leadingIcon = com.cairosquad.design_system.R.drawable.search_bottom_nav,
                    trailingIcon = com.cairosquad.design_system.R.drawable.more_bottom_nav,
                    isPassword = true,
                )
            }
        }
    }
}

