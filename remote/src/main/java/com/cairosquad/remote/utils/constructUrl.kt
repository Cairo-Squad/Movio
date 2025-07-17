package com.cairosquad.remote.utils

import com.cairosquad.remote.BuildConfig
import java.util.Locale

fun constructUrl(url: String): String {
    val baseUrl = BuildConfig.BASE_URL
    val deviceLanguage = Locale.getDefault().toLanguageTag()

    val fullUrl = when {
        url.contains(baseUrl) -> url
        url.startsWith("/") -> baseUrl + url.drop(1)
        else -> baseUrl + url
    }

    return if (fullUrl.contains("?")) {
        "$fullUrl&language=$deviceLanguage"
    } else {
        "$fullUrl?language=$deviceLanguage"
    }
}