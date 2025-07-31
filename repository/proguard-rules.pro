# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

##########################################
# 🌟 Kotlinx Serialization
##########################################
# Preserve Serialization generated code
-keep class kotlinx.serialization.** { *; }
-dontwarn kotlinx.serialization.**

-keep @kotlinx.serialization.Serializable class * {*;}
-keepclassmembers class ** {
    @kotlinx.serialization.Serializable *;
}

##########################################
# 🌟 Multi-Module References
##########################################
-keep class com.cairosquad.domain.** { *; }
-dontwarn com.cairosquad.domain.**

##########################################
# 🚫 Exclude Tests
##########################################
-dontwarn org.junit.**
-dontwarn com.google.common.truth.**
-dontwarn io.ktor.client.mock.**
-dontwarn java.lang.invoke.StringConcatFactory


# -keep class kotlinx.** { *; }
# -keepclassmembers class kotlinx.** { *; }

# Added Hilt-specific rules
-keep class dagger.hilt.** { *; }
-dontwarn dagger.hilt.**

-keep @dagger.hilt.android.AndroidEntryPoint class * { *; }
-keep @dagger.hilt.android.lifecycle.HiltViewModel class * { *; }
