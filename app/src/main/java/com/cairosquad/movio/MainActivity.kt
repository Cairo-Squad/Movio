package com.cairosquad.movio

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import com.google.firebase.analytics.FirebaseAnalytics

class MainActivity : ComponentActivity() {
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        firebaseAnalytics = FirebaseAnalytics.getInstance(this)

        val bundle = Bundle().apply {
            putString(FirebaseAnalytics.Param.METHOD, "test_button_click")
        }

        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN, bundle)
    }
}



