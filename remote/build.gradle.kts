import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kover)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.kapt)
}
android {
    namespace = "com.cairosquad.remote"
    compileSdk = 35

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    val properties = Properties()
    properties.load(rootProject.file("local.properties").inputStream())

    buildTypes {
        release {
            buildConfigField("String", "API_KEY", properties.getProperty("API_KEY"))
            buildConfigField("String", "BASE_URL", properties.getProperty("BASE_URL"))
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            buildConfigField("String", "API_KEY", properties.getProperty("API_KEY"))
            buildConfigField("String", "BASE_URL", properties.getProperty("BASE_URL"))
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
        buildConfig = true
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
    implementation(libs.serialization.json)
    implementation(libs.logging.interceptor)

    // --- Retrofit 3 ---
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.kotlinx.serialization)
    implementation (libs.okhttp)
    testImplementation(kotlin("test"))
    testImplementation(libs.mockwebserver)


    testImplementation(libs.coroutines.test)

    testImplementation(libs.junit.jupiter)
    testImplementation(libs.junit)
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${libs.versions.junitJupiter.get()}")
    testImplementation(libs.truth)
    testImplementation(libs.mockk)
    implementation(libs.logging.interceptor)

    // Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    implementation(project(":repository"))
}