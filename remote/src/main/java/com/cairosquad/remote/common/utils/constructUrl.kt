package com.cairosquad.remote.common.utils

import com.cairosquad.remote.common.Constants

fun constructUrl(url: String): String {
    return when {
        url.contains(Constants.BASE_URL) -> url
        url.startsWith("/") -> Constants.BASE_URL + url.drop(1)
        else -> Constants.BASE_URL + url
    }
}