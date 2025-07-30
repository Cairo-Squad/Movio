# Consumer ProGuard rules for Safe Image Viewer Library
# This file should be named: consumer-rules.pro
# Place this file in your library module root directory

# ==================== LIBRARY PUBLIC API ====================
# Keep all public classes and methods that are part of the library's API
-keep public class com.cairosquad.safe_image_viewer.** {
    public *;
}

# Keep the main NSFWDetector object and its public methods
-keep class com.cairosquad.safe_image_viewer.classifier.NSFWDetector {*; }

# Keep CoilImageLoader class as it's used by the library consumers
-keep class com.cairosquad.safe_image_viewer.loader.CoilImageLoader {
    public <init>(...);
    public *** loadBitmap(...);
}

# ==================== COMPOSE COMPONENTS ====================
# Keep all Composable functions that consumers will use
-keep @androidx.compose.runtime.Composable class * { *; }
-keep class * {
    @androidx.compose.runtime.Composable *;
}

# Keep SafeImageViewer composable specifically
-keep class * {
    *** SafeImageViewer(...);
}

# Keep Compose related annotations and classes
-keep @androidx.compose.runtime.Immutable class *
-keep @androidx.compose.runtime.Stable class *
-keep class androidx.compose.runtime.** { *; }

# ==================== FIREBASE ML VISION (for consumers) ====================
# These rules ensure Firebase ML works when the library is consumed
-keep class com.google.firebase.ml.vision.** { *; }
-keep class com.google.firebase.ml.vision.automl.** { *; }
-keep class com.google.firebase.ml.vision.label.** { *; }
-keep class com.google.firebase.ml.vision.common.** { *; }
-keep class com.google.firebase.ml.common.** { *; }

# Keep Firebase Vision Image classes
-keep class com.google.firebase.ml.vision.common.FirebaseVisionImage { *; }
-keep class com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata { *; }

# Prevent obfuscation of Firebase ML classes
-keepnames class com.google.firebase.ml.** { *; }

# ==================== TENSORFLOW LITE ====================
-keep class org.tensorflow.lite.** { *; }
-keep class org.tensorflow.lite.gpu.** { *; }
-keepclassmembers class org.tensorflow.lite.** { *; }

# ==================== COIL DEPENDENCIES ====================
# Keep Coil classes that consumers might interact with
-keep class coil.** { *; }
-keep interface coil.** { *; }
-keepclassmembers class coil.** { *; }

# Keep Coil's public API
-keep class coil.ImageLoader { *; }
-keep class coil.request.ImageRequest { *; }
-keep class coil.request.ImageRequest$Builder { *; }

# Keep Coil decoders and fetchers
-keep class coil.decode.** { *; }
-keep class coil.fetch.** { *; }
-keep class coil.transform.** { *; }

# ==================== ANDROID GRAPHICS ====================
# Keep Android graphics classes that might be exposed in the API
-keep class android.graphics.Bitmap { *; }
-keep class android.graphics.BitmapFactory { *; }
-keep class android.graphics.drawable.BitmapDrawable { *; }
-keep class androidx.core.graphics.** { *; }

# ==================== CALLBACK INTERFACES ====================
# Keep callback function types used in the library API
-keep class kotlin.jvm.functions.Function0 { *; }
-keep class kotlin.jvm.functions.Function1 { *; }
-keep class kotlin.jvm.functions.Function2 { *; }

# Keep lambda expressions and method references
-keep class * {
    *** lambda$*(...);
}

# ==================== KOTLIN SPECIFICS ====================
# Keep Kotlin metadata for proper interop
-keepattributes *Annotation*
-keepattributes Signature
-keepattributes InnerClasses
-keepattributes EnclosingMethod

# Keep Kotlin coroutines (if used in public API)
-keep class kotlinx.coroutines.** { *; }
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory { *; }
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler { *; }

# ==================== COMPOSE UI ELEMENTS ====================
# Keep UI-related classes that consumers might need
-keep class androidx.compose.ui.** { *; }
-keep class androidx.compose.foundation.** { *; }

# Keep Modifier and related classes
-keep class androidx.compose.ui.Modifier { *; }
-keep class androidx.compose.ui.Alignment { *; }
-keep class androidx.compose.ui.graphics.** { *; }
-keep class androidx.compose.ui.layout.ContentScale { *; }

# ==================== PAINTER AND DRAWABLE RESOURCES ====================
# Keep classes related to image resources that might be used in the API
-keep class androidx.compose.ui.graphics.painter.Painter { *; }
-keep class androidx.compose.ui.res.** { *; }

# ==================== DATA CLASSES AND ENUMS ====================
# Keep any data classes or enums that are part of the public API
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# Keep enum classes
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# ==================== REFLECTION SAFETY ====================
# Keep classes that might be accessed via reflection
-keepclassmembers class * {
    @androidx.annotation.Keep *;
}

# Keep native methods
-keepclasseswithmembernames class * {
    native <methods>;
}

# ==================== EXCEPTION HANDLING ====================
# Keep custom exceptions that might be thrown by the library
-keep public class * extends java.lang.Exception
-keep public class * extends java.lang.RuntimeException

# ==================== LOGGING AND DEBUGGING ====================
# Keep line numbers for crash reports
-keepattributes SourceFile,LineNumberTable

# ==================== WARNINGS SUPPRESSION ====================
# Don't warn about missing classes that are optional dependencies
-dontwarn org.tensorflow.lite.gpu.**
-dontwarn com.google.firebase.ml.vision.face.**
-dontwarn com.google.firebase.ml.vision.text.**
-dontwarn com.google.firebase.ml.vision.barcode.**
-dontwarn kotlinx.coroutines.**

# ==================== LIBRARY-SPECIFIC RULES ====================
# Keep any configuration classes or builders
-keep class * {
    public <init>(...);
}

# Keep public static factory methods
-keepclassmembers class * {
    public static *** create*(...);
    public static *** build*(...);
    public static *** getInstance(...);
}

# ==================== PARAMETER NAMES ====================
# Keep parameter names for better API documentation
-keepparameternames