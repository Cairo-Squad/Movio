
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
# 🌟 AndroidX Navigation
##########################################
-keep class androidx.navigation.** { *; }
-dontwarn androidx.navigation.**

##########################################
# 🌟 AndroidX Material3
##########################################
-keep class androidx.compose.material3.** { *; }
-dontwarn androidx.compose.material3.**

##########################################
# 🌟 AndroidX ConstraintLayout Compose
##########################################
-keep class androidx.constraintlayout.compose.** { *; }
-dontwarn androidx.constraintlayout.compose.**

##########################################
# 🌟 AndroidX Core
##########################################
-keep class androidx.core.** { *; }
-dontwarn androidx.core.**

##########################################
# 🌟 Kotlin Metadata
##########################################
-keep class kotlin.Metadata { *; }
-keepclassmembers class ** {
    @kotlin.Metadata *;
}

##########################################
# 🌟 Multi-Module Reference
###########################
-dontwarn kotlinx.**
-dontwarn okhttp3.**
-dontwarn org.jetbrains.kotlin.**
-keep class kotlinx.** { *; }
-keepclassmembers class kotlinx.** { *; }