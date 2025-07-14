##########################################
# 🌟 AndroidX Core
##########################################
-keep class androidx.core.** { *; }

##########################################
# 🌟 Coil
##########################################
-keep class coil.** { *; }
-keepclassmembers class * {
    @coil.annotation.* *;
}

##########################################
# 🌟 TensorFlow Lite
##########################################
-keep class org.tensorflow.lite.** { *; }

##########################################
# 🌟 TensorFlow Lite Support API
##########################################
-keep class org.tensorflow.lite.support.** { *; }

##########################################
# 🌟 Kotlin Metadata
##########################################
-keep class kotlin.Metadata { *; }
-keepclassmembers class ** {
    @kotlin.Metadata *;
}