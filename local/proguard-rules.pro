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
# 🌟 Room Database
##########################################
# Keep Room generated classes (DAOs, Database_Impl, etc.)
-keep class androidx.room.** { *; }
-dontwarn androidx.room.**

# Keep @Dao, @Entity, @Database annotated classes
-keep @androidx.room.* class * {*;}
-keepclassmembers class * {
    @androidx.room.* *;
}

# Keep Kotlin Metadata for Room's reflection
-keep class kotlin.Metadata { *; }
-keepclassmembers class ** {
    @kotlin.Metadata *;
}

##########################################
# 🌟 Multi-Module References
##########################################
-keep class com.cairosquad.repository.** { *; }
-dontwarn com.cairosquad.repository.**

##########################################
# 🚫 Exclude Tests
##########################################
-dontwarn io.mockk.**
-dontwarn com.google.common.truth.**
-dontwarn org.junit.**

-dontwarn java.lang.invoke.StringConcatFactory
-dontwarn kotlinx.**
-dontwarn okhttp3.**
-dontwarn org.jetbrains.kotlin.**
-keep class kotlinx.** { *; }
-keepclassmembers class kotlinx.** { *; }