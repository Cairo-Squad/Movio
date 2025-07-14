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
# 🌟 Koin
##########################################
-keep class org.koin.** { *; }
-keep @org.koin.core.annotation.* class * {*;}
-keepclassmembers class * {
    @org.koin.core.annotation.* *;
}

##########################################
# 🌟 Kotlin Metadata
##########################################
-keep class kotlin.Metadata { *; }
-keepclassmembers class ** {
    @kotlin.Metadata *;
}
