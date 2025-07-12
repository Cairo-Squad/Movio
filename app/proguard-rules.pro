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

# Keep Compose parameter names for reflection
-keepattributes SourceFile,LineNumberTable,Signature,LocalVariableTable,LocalVariableTypeTable,RuntimeVisibleAnnotations,RuntimeVisibleParameterAnnotations

##########################################
# 🌟 AndroidX Lifecycle & Core
##########################################
-keep class androidx.lifecycle.** { *; }
-dontwarn androidx.lifecycle.**

-keep class androidx.core.** { *; }
-dontwarn androidx.core.**

##########################################
# 🌟 Navigation Compose
##########################################
-keep class androidx.navigation.** { *; }
-dontwarn androidx.navigation.**

##########################################
# 🌟 Firebase
##########################################
# Analytics, Crashlytics, Performance Monitoring
-keep class com.google.firebase.** { *; }
-dontwarn com.google.firebase.**
-keep class com.google.android.gms.** { *; }
-dontwarn com.google.android.gms.**

# Keep Firebase Performance monitoring annotations
-keep @com.google.firebase.perf.metrics.AddTrace class * {*;}
-keepclassmembers class * {
    @com.google.firebase.perf.metrics.AddTrace *;
}

##########################################
# 🌟 Koin (Reflection heavy)
##########################################
-keep class org.koin.** { *; }
-dontwarn org.koin.**

# Keep DI annotations
-keep @org.koin.core.annotation.* class * {*;}
-keepclassmembers class * {
    @org.koin.core.annotation.* *;
}

##########################################
# 🌟 Ktor
##########################################
-keep class io.ktor.** { *; }
-dontwarn io.ktor.**

# Keep OkHttp and Logging
-keep class okhttp3.** { *; }
-dontwarn okhttp3.**

##########################################
# 🌟 Room
##########################################
# Keep Room generated classes
-keep class androidx.room.** { *; }
-dontwarn androidx.room.**

# Keep @Entity, @Dao, @Database
-keep @androidx.room.* class * {*;}
-keepclassmembers class * {
    @androidx.room.* *;
}

# Keep RoomOpenHelper delegate classes
-keep class * extends androidx.room.RoomOpenHelper$Delegate { *; }

##########################################
# 🌟 Kotlin Metadata
##########################################
-keep class kotlin.Metadata { *; }
-keepclassmembers class ** {
    @kotlin.Metadata *;
}

##########################################
# 🌟 Multi-Module References
##########################################
-keep class com.cairosquad.design_system.** { *; }
-keep class com.cairosquad.domain.** { *; }
-keep class com.cairosquad.entity.** { *; }
-keep class com.cairosquad.remote.** { *; }
-keep class com.cairosquad.local.** { *; }
-keep class com.cairosquad.repository.** { *; }
-keep class com.cairosquad.viewmodel.** { *; }
-keep class com.cairosquad.ui.** { *; }
-dontwarn com.cairosquad.**
-dontwarn a.a
-ignorewarnings