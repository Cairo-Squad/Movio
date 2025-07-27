import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.cairosquad.ui"
    compileSdk = 35

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }
    val properties = Properties()
    properties.load(rootProject.file("secret.properties").inputStream())
    properties.getProperty("IMAGE_BASE_URL")

    buildTypes {
        release {
            buildConfigField("String", "IMAGE_BASE_URL", properties.getProperty("IMAGE_BASE_URL"))

            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            buildConfigField("String", "IMAGE_BASE_URL", properties.getProperty("IMAGE_BASE_URL"))
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
        buildConfig = true
    }
}
dependencies {
    implementation(projects.designSystem)
    implementation(projects.viewmodel)
    implementation(projects.safeImageViewer)

    implementation(libs.bundles.androidcore)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.composeUi)
    debugImplementation(libs.bundles.composedebug)

    implementation(libs.bundles.koin)
    ksp(libs.koin.ksp)

    implementation(libs.navigation.compose)

    implementation(libs.kotlinx.serialization.json)
}