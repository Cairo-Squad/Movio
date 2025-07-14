##########################################
# 🌟 Room Database
##########################################
# Keep Room generated classes (DAOs, Database_Impl, etc.)
-keep class androidx.room.** { *; }
-keep @androidx.room.* class * {*;}
-keepclassmembers class * {
    @androidx.room.* *;
}

##########################################
# 🌟 Kotlin Metadata
##########################################
-keep class kotlin.Metadata { *; }
-keepclassmembers class ** {
    @kotlin.Metadata *;
}

##########################################
# 🌟 AndroidX Core
##########################################
-keep class androidx.core.** { *; }
