# Preserve ViewModel classes in consumer app
-keep class * extends androidx.lifecycle.ViewModel

# Preserve Lifecycle observers and annotations
-keep class androidx.lifecycle.** { *; }
-keep @androidx.lifecycle.* class * {*;}

# Preserve Kotlin metadata for reflection
-keep class kotlin.Metadata { *; }
-keepclassmembers class ** {
    @kotlin.Metadata *;
}

# Keep Core KTX extension functions
-keep class androidx.core.** { *; }

# Prevent consumer’s R8 from stripping coroutines
-keep class kotlinx.coroutines.** { *; }
