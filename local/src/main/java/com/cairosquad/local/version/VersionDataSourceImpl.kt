package com.cairosquad.local.version

import android.content.Context
import com.cairosquad.repository.version.VersionDataSource
import javax.inject.Inject

class VersionDataSourceImpl @Inject constructor(
    private val context: Context
) : VersionDataSource {
    override fun getAppVersion(): String {
        val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        return packageInfo.versionName ?: "Unknown"
    }
}