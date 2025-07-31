##########################################
# 🌟 Jetpack Compose (needed for consumers)
##########################################
-keep class androidx.compose.** { *; }
-keep @androidx.compose.ui.tooling.preview.Preview class * {*;}
-keepclassmembers class * {
    @androidx.compose.ui.tooling.preview.Preview *;
}

##########################################
# 🌟 AndroidX Lifecycle
##########################################
-keep class androidx.lifecycle.** { *; }
-keepclassmembers class * extends androidx.lifecycle.ViewModel {
    <init>(...);
}

##########################################
# 🌟 Dagger Hilt
##########################################
# Keep Hilt's generated code
-keep class dagger.hilt.** { *; }
-dontwarn dagger.hilt.**

# Preserve Hilt entry points (activities, fragments, services)
-keep @dagger.hilt.android.AndroidEntryPoint class * { *; }

# Preserve Hilt ViewModel annotations
-keep @dagger.hilt.android.lifecycle.HiltViewModel class * { *; }

# Optional: Add if you use Hilt's @EntryPoint or @InstallIn
-keep @dagger.hilt.EntryPoint class * { *; }
-keep @dagger.hilt.InstallIn class * { *; }

# Optional: Add if you use Hilt's @HiltAndroidApp in the app module
-keep @dagger.hilt.android.HiltAndroidApp class * { *; }

##########################################
# 🌟 Kotlin Metadata
##########################################
-keep class kotlin.Metadata { *; }
-keepclassmembers class ** {
    @kotlin.Metadata *;
}
-keep @dagger.hilt.EntryPoint class * { *; }
-keep @dagger.hilt.InstallIn class * { *; }