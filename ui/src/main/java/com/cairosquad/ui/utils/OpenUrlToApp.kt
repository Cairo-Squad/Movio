package com.cairosquad.ui.utils

import android.content.Context
import android.content.Intent
import android.net.Uri

fun openUrlInAppOrBrowser(
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