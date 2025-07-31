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
# 🌟 Jetpack Compose
##########################################
# Keep Compose compiler-generated classes
-keep class androidx.compose.** { *; }
-dontwarn androidx.compose.**

# Keep Compose @Preview annotations (debug only)
-keep @androidx.compose.ui.tooling.preview.Preview class * {*;}
-keepclassmembers class * {
    @androidx.compose.ui.tooling.preview.Preview *;
}

# Keep Compose parameter names (needed for reflection in tooling)
-keepattributes SourceFile,LineNumberTable,Signature,LocalVariableTable,LocalVariableTypeTable,RuntimeVisibleAnnotations,RuntimeVisibleParameterAnnotations

##########################################
# 🌟 AndroidX Lifecycle
##########################################
-keep class androidx.lifecycle.** { *; }
-dontwarn androidx.lifecycle.**

-keepclassmembers class * extends androidx.lifecycle.ViewModel {
    <init>(...);
}

##########################################
# 🌟 Kotlin Metadata
##########################################
-keep class kotlin.Metadata { *; }
-keepclassmembers class ** {
    @kotlin.Metadata *;
}

##########################################
# 🌟 Dagger Hilt
##########################################
# Keep Hilt's generated code
-keep class dagger.hilt.** { *; }
-dontwarn dagger.hilt.**

# Preserve Hilt entry points (activities, fragments, etc.)
-keep @dagger.hilt.android.AndroidEntryPoint class * { *; }

# Preserve Hilt ViewModel annotations
-keep @dagger.hilt.android.lifecycle.HiltViewModel class * { *; }
# Optional: Add if you use Hilt's @EntryPoint or @InstallIn
-keep @dagger.hilt.EntryPoint class * { *; }
-keep @dagger.hilt.InstallIn class * { *; }

# Optional: Add if you use Hilt's @HiltAndroidApp in the app module
-keep @dagger.hilt.android.HiltAndroidApp class * { *; }

##########################################
# 🌟 Multi-module dependencies
##########################################
-keep class com.cairosquad.design_system.** { *; }
-keep class com.cairosquad.viewmodel.** { *; }
-keep class com.cairosquad.domain.** { *; }
-dontwarn com.cairosquad.**

# Keep dependencies for Ktor, OkHttp, etc.
-dontwarn okhttp3.**
-dontwarn org.jetbrains.kotlin.**

-keep @dagger.hilt.android.HiltAndroidApp class * { *; }
-keep @dagger.hilt.EntryPoint class * { *; }
-keep @dagger.hilt.InstallIn class * { *; }
