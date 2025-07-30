# ProGuard rules for Safe Image Viewer with NSFW Detection
# Add these rules to your proguard-rules.pro file

# ==================== FIREBASE ML VISION ====================
# Keep Firebase ML Vision classes
-keep class com.google.firebase.ml.vision.** { *; }
-keep class com.google.firebase.ml.common.** { *; }

# Keep AutoML model classes
-keep class com.google.firebase.ml.vision.automl.** { *; }
-keep class com.google.firebase.ml.vision.label.** { *; }
-keep class com.google.firebase.ml.vision.common.** { *; }

# Keep Firebase Vision Image classes
-keep class com.google.firebase.ml.vision.common.FirebaseVisionImage { *; }
-keep class com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata { *; }

# Prevent obfuscation of Firebase ML classes
-keepnames class com.google.firebase.ml.** { *; }

# ==================== TENSORFLOW LITE (used by Firebase ML) ====================
-keep class org.tensorflow.lite.** { *; }
-keep class org.tensorflow.lite.gpu.** { *; }
-keepclassmembers class org.tensorflow.lite.** { *; }

# ==================== YOUR NSFW DETECTOR ====================
# Keep your NSFWDetector class and its methods
-keep class com.cairosquad.safe_image_viewer.classifier.NSFWDetector { *;}

# Keep CoilImageLoader class
-keep class com.cairosquad.safe_image_viewer.loader.CoilImageLoader {
    public <init>(...);
    public *** loadBitmap(...);
}

# ==================== COIL IMAGE LOADING ====================
# Keep Coil classes
-keep class coil.** { *; }
-keep interface coil.** { *; }
-keepclassmembers class coil.** { *; }

# Keep Coil's internal classes that might be used via reflection
-keep class coil.decode.** { *; }
-keep class coil.fetch.** { *; }
-keep class coil.transform.** { *; }
-keep class coil.request.** { *; }
-keep class coil.size.** { *; }

# Prevent obfuscation of Coil's ImageLoader and related classes
-keepnames class coil.ImageLoader
-keepnames class coil.request.ImageRequest
-keepnames class coil.request.ImageRequest$Builder

# ==================== ANDROID GRAPHICS ====================
# Keep Bitmap and related graphics classes
-keep class android.graphics.Bitmap { *; }
-keep class android.graphics.BitmapFactory { *; }
-keep class android.graphics.drawable.BitmapDrawable { *; }
-keep class androidx.core.graphics.** { *; }

# ==================== ANDROID COMPONENTS ====================
# Keep Context and related classes
-keep class android.content.Context { *; }
-keep class android.os.Handler { *; }
-keep class android.os.Looper { *; }
-keep class android.util.LruCache { *; }
-keep class android.util.Log { *; }

# ==================== COMPOSE RUNTIME ====================
# Keep Compose runtime classes used in your code
-keep class androidx.compose.runtime.** { *; }
-keepclassmembers class androidx.compose.runtime.** { *; }

# Keep @Immutable annotation
-keep @androidx.compose.runtime.Immutable class *

# ==================== JAVA CONCURRENCY ====================
# Keep ExecutorService and related classes
-keep class java.util.concurrent.** { *; }
-keep class java.util.concurrent.Executors { *; }

# ==================== KOTLIN COROUTINES ====================
# Keep coroutines classes (in case you use them)
-keep class kotlinx.coroutines.** { *; }
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory { *; }
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler { *; }

# ==================== GENERAL OPTIMIZATIONS ====================
# Don't warn about missing classes that might not be used
-dontwarn org.tensorflow.lite.gpu.**
-dontwarn com.google.firebase.ml.vision.face.**
-dontwarn com.google.firebase.ml.vision.text.**
-dontwarn com.google.firebase.ml.vision.barcode.**

# Keep line numbers for debugging
-keepattributes SourceFile,LineNumberTable

# Keep generic signatures for better debugging
-keepattributes Signature

# Keep annotations
-keepattributes *Annotation*

# ==================== LAMBDA EXPRESSIONS ====================
# Keep lambda expressions and method references
-keep class * {
    *** lambda$*(...);
}

# ==================== REFLECTION PREVENTION ====================
# Prevent reflection on sensitive classes
-keepclassmembers class com.cairosquad.safe_image_viewer.classifier.NSFWDetector {
    !private <fields>;
    !private <methods>;
}

# ==================== OPTIMIZATION SETTINGS ====================
# Enable aggressive optimizations
-optimizations !code/simplification/arithmetic,!code/simplification/cast,!field/*,!class/merging/*
-optimizationpasses 5
-allowaccessmodification
-dontpreverify

# ==================== ADDITIONAL SAFETY RULES ====================
# Keep native methods
-keepclasseswithmembernames class * {
    native <methods>;
}

# Keep custom exceptions
-keep public class * extends java.lang.Exception

# Keep callback interfaces and their methods
-keep interface * {
    public <methods>;
}

# Keep classes with special naming patterns
-keepnames class * implements android.os.Parcelable
-keepnames class * implements java.io.Serializable