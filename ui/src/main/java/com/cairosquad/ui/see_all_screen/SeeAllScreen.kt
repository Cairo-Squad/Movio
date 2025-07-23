package com.cairosquad.ui.see_all_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.cairosquad.design_system.basic_component.Text
import com.cairosquad.viewmodel.util.MediaContentType
import com.cairosquad.viewmodel.util.MediaType

@Composable
fun SeeAllScreen(
    contentType: MediaContentType,
    mediaType: MediaType
){
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Text("this is the see all screen\n\n$contentType\n\n$mediaType")
    }

}