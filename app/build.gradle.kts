import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.google.firebase.crashlytics)
    alias(libs.plugins.google.firebase.perf)
    alias(libs.plugins.google.firebase.appdistribution)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.androidx.room)
}

android {
    namespace = "com.cairosquad.movio"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.cairosquad.movio"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    val secretsFile = file("${rootProject.projectDir}/secret.properties")
    val secrets = Properties().apply {
        if (secretsFile.exists()) {
            load(FileInputStream(secretsFile))
        }
    }

    signingConfigs {
        create("release") {
            storeFile = file("movio-cairo.jks")
            storePassword = secrets.getProperty("KEYSTORE_PASSWORD")
            keyAlias = secrets.getProperty("KEY_ALIAS")
            keyPassword = secrets.getProperty("KEY_PASSWORD")
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
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
        }
    }
    buildFeatures {
        compose = true
    }
    room {
        schemaDirectory("schemas")
    }
}

ksp {
    arg("KOIN_CONFIG_CHECK", "true")
}

dependencies {
    // Project Modules
    implementation(projects.designSystem)
    implementation(projects.domain)
    implementation(projects.entity)
    implementation(projects.local)
    implementation(projects.remote)
    implementation(projects.repository)
    implementation(projects.ui)
    implementation(projects.viewmodel)

    // AndroidX Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.foundation)
    implementation(libs.androidx.foundation.layout.android)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.ui.tooling.preview)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Core and Lifecycle
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.perf)

    // Dependency Injection
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)
    implementation(libs.koin.annotations)
    implementation(libs.koin.test)
    ksp(libs.koin.ksp)

    // Retrofit and Networking
    implementation(libs.retrofit)
    implementation(libs.converter.kotlinx.serialization)
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)

    // Serialization
    implementation(libs.kotlinx.serialization.json)

    // Room Database
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)
}
