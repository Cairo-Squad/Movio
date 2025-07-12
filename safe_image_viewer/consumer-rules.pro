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
-keep class org.tensorflow.lite.support.** { *; }
-keep class org.tensorflow.lite.task.vision.** { *; }

##########################################
# 🌟 Kotlin Metadata
##########################################
-keep class kotlin.Metadata { *; }
-keepclassmembers class ** {
    @kotlin.Metadata *;
}
