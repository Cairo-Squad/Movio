@file:Suppress("DEPRECATION")

package com.cairosquad.design_system.basic_component

import android.annotation.SuppressLint
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebView(
    webPageUrl: String, modifier: Modifier = Modifier, onBackPressed: (() -> Unit)? = null
) {
    var webView by remember { mutableStateOf<WebView?>(null) }

    BackHandler(enabled = true) {
        if (webView?.canGoBack() == true) {
            webView?.goBack()
        } else {
            onBackPressed?.invoke()
        }
    }

    AndroidView(
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                webViewClient = object : WebViewClient() {
                    override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                        val uri = request.url
                        val isTmdbDomain = uri.host?.contains("themoviedb.org") == true

                        return if (isTmdbDomain) {
                            false
                        } else {
                            Toast.makeText(context, "Navigation blocked", Toast.LENGTH_SHORT).show()
                            true
                        }
                    }

                }

                loadUrl(webPageUrl)
                webView = this
            }
        }, modifier = modifier
    )
}