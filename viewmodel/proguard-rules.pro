# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
#############################
# AndroidX Core
#############################
-keep class androidx.core.** { *; }
-dontwarn androidx.core.**

#############################
# AndroidX Lifecycle
#############################
# Keep ViewModel and Lifecycle components
-keep class androidx.lifecycle.** { *; }
-dontwarn androidx.lifecycle.**

# Keep ViewModel with SavedStateHandle
-keepclassmembers class * extends androidx.lifecycle.ViewModel {
    <init>(...);
}

#############################
# Kotlin Coroutines
#############################
# Keep suspend functions metadata
-keepclassmembers class kotlinx.coroutines.** {
    @kotlin.Metadata *;
}

-keep class kotlinx.coroutines.** { *; }
-dontwarn kotlinx.coroutines.**

#############################
# Room (if used later)
#############################
# -keep class androidx.room.** { *; }
# -dontwarn androidx.room.**

#############################
# Google Truth
#############################
# No runtime impact – safe to ignore for release builds

#############################
# MockK
#############################
# MockK is test-only, ignore it for release
-dontwarn io.mockk.**

#############################
# JUnit (test only)
#############################
-dontwarn org.junit.**

-dontwarn kotlinx.**
-dontwarn okhttp3.**
-dontwarn org.jetbrains.kotlin.**
-keep class kotlinx.** { *; }
-keepclassmembers class kotlinx.** { *; }