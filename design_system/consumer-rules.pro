##########################################
# 🌟 Jetpack Compose
##########################################
-keep class androidx.compose.** { *; }
-keep @androidx.compose.ui.tooling.preview.Preview class * {*;}
-keepclassmembers class * {
    @androidx.compose.ui.tooling.preview.Preview *;
}

##########################################
# 🌟 AndroidX Navigation
##########################################
-keep class androidx.navigation.** { *; }

##########################################
# 🌟 AndroidX Material3
##########################################
-keep class androidx.compose.material3.** { *; }

##########################################
# 🌟 AndroidX ConstraintLayout Compose
##########################################
-keep class androidx.constraintlayout.compose.** { *; }

##########################################
# 🌟 AndroidX Core
##########################################
-keep class androidx.core.** { *; }

##########################################
# 🌟 Kotlin Metadata
##########################################
-keep class kotlin.Metadata { *; }
-keepclassmembers class ** {
    @kotlin.Metadata *;
}
