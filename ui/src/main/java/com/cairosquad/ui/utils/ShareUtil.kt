package com.cairosquad.ui.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.widget.Toast
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

    fun copyLink(seriesUrl: String, context: Context, isSeries: Boolean, onDismiss: () -> Unit) {
        try {
            val label =
                context.getString(if (isSeries) R.string.series_link else R.string.movie_link)
            val clipboard =
                context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
            val clip = ClipData.newPlainText(label, seriesUrl)
            clipboard?.setPrimaryClip(clip)
            onDismiss()
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
                Toast.makeText(
                    context,
                    R.string.copied_to_clipboard_successfully,
                    Toast.LENGTH_SHORT
                ).show()
            }

        } catch (_: Exception) {
            onDismiss()
            Toast.makeText(context, R.string.copied_to_clipboard_unsuccessfully, Toast.LENGTH_SHORT)
                .show()
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