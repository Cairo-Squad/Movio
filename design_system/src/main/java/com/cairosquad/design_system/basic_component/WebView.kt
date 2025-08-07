@file:Suppress("DEPRECATION")

package com.cairosquad.design_system.basic_component

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.viewinterop.AndroidView
import com.cairosquad.design_system.R

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebView(
    webPageUrl: String,
    modifier: Modifier = Modifier,
    onBackPressed: (() -> Unit)? = null,
    onBlockedNavigation: (() -> Unit)? = null

) {
    var webView by remember { mutableStateOf<WebView?>(null) }
    var snackBarMessage by remember { mutableStateOf<String?>(null) }

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
                    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                        val uri = Uri.parse(url)
                        val isTmdbDomain = uri.host == "www.themoviedb.org"
                        val allowedPaths = listOf("signup", "reset-password")
                        val isAllowed = isTmdbDomain && (uri.path in allowedPaths)

                        if (!isAllowed) {
                            Log.d("WebView", "Navigation blocked: $url")
                            onBlockedNavigation?.invoke()
                        }

                        return !isAllowed // true = block, false = allow
                    }
                }

                loadUrl(webPageUrl)
                webView = this
            }
        }, modifier = modifier
    )
    AnimatedVisibility(visible = snackBarMessage != null) {
        SnackBar(
            message = snackBarMessage.orEmpty(),
            imageVector = ImageVector.vectorResource(id = R.drawable.danger),
            action = { snackBarMessage = null }
        )
    }
}