import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    id("org.jetbrains.kotlin.kapt")
    id("androidx.room") version "2.7.1"
}

android {
    namespace = "com.cairosquad.local"
    compileSdk = 35

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
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
    room {
        schemaDirectory("schemas")
    }
    packaging {
        resources {
            excludes += arrayOf(
                "/META-INF/{AL2.0,LGPL2.1}",
                "META-INF/LICENSE.md",
                "META-INF/LICENSE",
                "META-INF/NOTICE",
                "META-INF/LICENSE-notice",
                "META-INF/LICENSE-notice.md"
            )
        }
    }
}

dependencies {

    implementation(libs.core.ktx)
    implementation(libs.test.junit.ktx)
    implementation(libs.datastore.preferences)
    implementation(libs.datastore.core)
    testImplementation(libs.junit)
    androidTestImplementation(libs.test.junit)

    // Room
    implementation(libs.room.runtime)
    ksp(libs.room.compiler)
    implementation(libs.room.ktx)

    testImplementation(libs.mockk)
    testImplementation(libs.truth)
    testImplementation(libs.coroutines.test)
    testImplementation(libs.room.testing)
    testImplementation(libs.core.testing)

    androidTestImplementation(libs.room.testing)
    androidTestImplementation(libs.runner)
    androidTestImplementation(libs.coroutines.test)
    androidTestImplementation(libs.core.testing)
    androidTestImplementation(libs.mockk)
    androidTestImplementation(libs.truth)

    // Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    // Data Store
    implementation(libs.bundles.datastore)


    implementation(project(":repository"))
    testImplementation(kotlin("test"))
}