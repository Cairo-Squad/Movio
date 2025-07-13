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
##########################################
# 🌟 AndroidX Core
##########################################
-keep class androidx.core.** { *; }
-dontwarn androidx.core.**

##########################################
# 🌟 Coil
##########################################
-keep class coil.** { *; }
-dontwarn coil.**

-keepclassmembers class * {
    @coil.annotation.* *;
}

##########################################
# 🌟 TensorFlow Lite
##########################################
-keep class org.tensorflow.lite.** { *; }
-dontwarn org.tensorflow.lite.**

##########################################
# 🌟 TensorFlow Lite Support API
##########################################
-keep class org.tensorflow.lite.support.** { *; }
-dontwarn org.tensorflow.lite.support.**

-keep class org.tensorflow.lite.support.image.** { *; }
-dontwarn org.tensorflow.lite.support.image.**

-keep class org.tensorflow.lite.support.tensorbuffer.** { *; }
-dontwarn org.tensorflow.lite.support.tensorbuffer.**

##########################################
# 🌟 Kotlin Metadata (required for reflection)
##########################################
-keep class kotlin.Metadata { *; }
-keepclassmembers class ** {
    @kotlin.Metadata *;
}

##########################################
# 🚫 Exclude Tests (MockK, Truth, JUnit)
##########################################
-dontwarn io.mockk.**
-dontwarn com.google.common.truth.**
-dontwarn org.junit.**
-dontwarn kotlinx.**