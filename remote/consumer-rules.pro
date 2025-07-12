##########################################
# 🌟 Kotlinx Serialization
##########################################
-keep class kotlinx.serialization.** { *; }
-keep @kotlinx.serialization.Serializable class * {*;}
-keepclassmembers class ** {
    @kotlinx.serialization.Serializable *;
}

##########################################
# 🌟 AndroidX DataStore
##########################################
-keep class androidx.datastore.** { *; }

##########################################
# 🌟 Ktor
##########################################
-keep class io.ktor.** { *; }

##########################################
# 🌟 OkHttp Logging Interceptor
##########################################
-keep class okhttp3.** { *; }

##########################################
# 🌟 Kotlin Metadata
##########################################
-keep class kotlin.Metadata { *; }
-keepclassmembers class ** {
    @kotlin.Metadata *;
}
