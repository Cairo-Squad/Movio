##########################################
# 🌟 Kotlinx Serialization
##########################################
-keep class kotlinx.serialization.** { *; }
-keep @kotlinx.serialization.Serializable class * {*;}
-keepclassmembers class ** {
    @kotlinx.serialization.Serializable *;
}
