package com.cairosquad.ui.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.net.toUri
import com.cairosquad.ui.R

object ShareUtil {

    fun shareOnFacebook(encodedMessageAndUrl: String, context: Context, onDismiss: () -> Unit) {
        val deepLinkUri = ("fb://facewebmodal/f?href=$encodedMessageAndUrl").toUri()
        val webUri =
            ("https://www.facebook.com/sharer/sharer.php?u=$encodedMessageAndUrl").toUri()
        openUrlInAppOrBrowser(
            appPackage = "com.facebook.katana",
            deepLinkUri = deepLinkUri,
            webUri = webUri,
            context = context
        )
        onDismiss()
    }

    fun shareOnX(encodedMessageAndUrl: String, context: Context, onDismiss: () -> Unit) {
        val deepLinkUri = ("twitter://post?message=$encodedMessageAndUrl").toUri()
        val webUri =
            ("https://twitter.com/intent/tweet?url=$encodedMessageAndUrl").toUri()
        openUrlInAppOrBrowser(
            appPackage = "com.twitter.android",
            deepLinkUri = deepLinkUri,
            webUri = webUri,
            context = context
        )
        onDismiss()
    }

    fun copyLink(seriesUrl: String, context: Context, onDismiss: (Int, Boolean) -> Unit) {
        try {
            val clipboard =
                context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
            val clip = ClipData.newPlainText("Series link", seriesUrl)
            clipboard?.setPrimaryClip(clip)
            onDismiss(R.string.copied_to_clipboard_successfully, true)
        } catch (_: Exception) {
            onDismiss(R.string.copied_to_clipboard_successfully, false)
        }
    }

    fun playOnYoutube(videoId: String, context: Context) {
        val deepLinkUri = "vnd.youtube:$videoId".toUri()
        val webUri = "https://www.youtube.com/watch?v=$videoId".toUri()

        if (videoId.isNotBlank()) {
            openUrlInAppOrBrowser(
                appPackage = "com.google.android.youtube",
                deepLinkUri = deepLinkUri,
                webUri = webUri,
                context = context
            )
        }
    }

    private fun openUrlInAppOrBrowser(
        appPackage: String,
        deepLinkUri: Uri,
        webUri: Uri,
        context: Context
    ) {
        val appIntent = Intent(Intent.ACTION_VIEW, deepLinkUri).apply {
            `package` = appPackage
        }
        val resolved = context.packageManager.queryIntentActivities(appIntent, 0)
        if (resolved.isNotEmpty()) {
            context.startActivity(appIntent)
        } else {
            context.startActivity(Intent(Intent.ACTION_VIEW, webUri))
        }
    }
}