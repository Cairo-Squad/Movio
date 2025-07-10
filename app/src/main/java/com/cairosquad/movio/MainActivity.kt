package com.cairosquad.movio

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import com.cairosquad.domain.search.usecase.GetExploreMoreUseCase
import org.koin.android.ext.android.getKoin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LaunchedEffect(true) {
                val getExploreMoreUseCase: GetExploreMoreUseCase = getKoin().get()

                Log.i("Test", "onCreate: ${getExploreMoreUseCase.getExploreMoreMovies()}")
            }
        }
    }
}

