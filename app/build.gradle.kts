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
    id("androidx.room") version "2.7.1"
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
    kotlinOptions {
        jvmTarget = "11"
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
    testImplementation(libs.junit)
    implementation(libs.androidx.ui)
    implementation(libs.firebase.perf)
    implementation(libs.androidx.core.ktx)
    implementation(libs.firebase.analytics)
    implementation(libs.androidx.foundation)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.firebase.crashlytics)
    implementation(platform(libs.firebase.bom))
    debugImplementation(libs.androidx.ui.tooling)
    androidTestImplementation(libs.ui.test.junit4)
    implementation(libs.androidx.activity.compose)
    androidTestImplementation(libs.androidx.junit)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.lifecycle.runtime.ktx)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(libs.androidx.foundation.layout.android)

    // koin
    implementation(libs.koin.androidx.compose)
    implementation(libs.koin.android)
    implementation(libs.koin.test)
    implementation(libs.koin.annotations)
    ksp(libs.koin.ksp)

    // ktor
    implementation(libs.bundles.ktor)
    implementation(libs.logging.interceptor)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.logging.interceptor)

    // Room
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

    implementation(project(":design_system"))
    implementation(project(":domain"))
    implementation(project(":entity"))
    implementation(project(":remote"))
    implementation(project(":local"))
    implementation(project(":repository"))
    implementation(project(":ui"))
    implementation(project(":viewmodel"))
}
