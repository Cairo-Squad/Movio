import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.google.firebase.crashlytics)
    alias(libs.plugins.google.firebase.perf)
    alias(libs.plugins.google.firebase.appdistribution)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.cairosquad.movio"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.cairosquad.movio"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.1.4"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    val localFile = file("${rootProject.projectDir}/local.properties")
    val locals = Properties().apply {
        if (localFile.exists()) {
            load(FileInputStream(localFile))
        }
    }

    signingConfigs {
        create("release") {
            storeFile = file("movio-cairo.jks")
            storePassword = locals.getProperty("KEYSTORE_PASSWORD")
            keyAlias = locals.getProperty("KEY_ALIAS")
            keyPassword = locals.getProperty("KEY_PASSWORD")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
            ndk {
                abiFilters += listOf("armeabi-v7a", "arm64-v8a")
            }
        }
        debug {
            isMinifyEnabled = false
            isShrinkResources = false
            ndk {
                abiFilters += listOf(
                    "armeabi-v7a",
                    "arm64-v8a",
                    "x86",
                    "x86_64"
                )
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    buildFeatures {
        compose = true
    }
    room {
        schemaDirectory("schemas")
    }
}

hilt {
    enableAggregatingTask = false
}

dependencies {
    testImplementation(libs.junit)
    implementation(libs.compose.ui)
    implementation(libs.firebase.perf)
    implementation(libs.core.ktx)
    implementation(libs.firebase.analytics)
    implementation(libs.compose.foundation)
    implementation(libs.compose.ui.graphics)
    implementation(libs.firebase.crashlytics)
    implementation(platform(libs.firebase.bom))
    debugImplementation(libs.compose.ui.tooling)
    androidTestImplementation(libs.compose.ui.test.junit4)
    implementation(libs.activity.compose)
    androidTestImplementation(libs.test.junit)
    implementation(libs.compose.ui.tooling.preview)
    implementation(platform(libs.compose.bom))
    implementation(libs.lifecycle.runtime.ktx)
    debugImplementation(libs.compose.ui.test.manifest)
    implementation(libs.compose.foundation.layout)

    implementation(libs.logging.interceptor)

    // --- Retrofit 3 ---
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.kotlinx.serialization)
    implementation(libs.serialization.json)
    implementation (libs.okhttp)

    // Room
    implementation(libs.room.runtime)
    ksp(libs.room.compiler)
    implementation(libs.room.ktx)

    // Dagger & Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    kapt(libs.hilt.androidx.compiler)
    implementation(libs.dagger)
    kapt(libs.dagger.compiler)

    implementation(project(":design_system"))
    implementation(project(":domain"))
    implementation(project(":entity"))
    implementation(project(":remote"))
    implementation(project(":local"))
    implementation(project(":repository"))
    implementation(project(":ui"))
    implementation(project(":viewmodel"))
}