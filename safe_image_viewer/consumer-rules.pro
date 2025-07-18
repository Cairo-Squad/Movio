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
-keep class org.tensorflow.lite.task.vision.classifier.ImageClassifier { *; }
-keep class org.tensorflow.lite.support.common.FileUtil { *; }
-keep class org.tensorflow.lite.support.image.TensorImage { *; }
-keep class org.tensorflow.lite.task.core.BaseOptions { *; }

##########################################
# 🌟 Kotlin Metadata
##########################################
-keep class kotlin.Metadata { *; }
-keepclassmembers class ** {
    @kotlin.Metadata *;
}