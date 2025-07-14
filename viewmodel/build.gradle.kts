plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kover)
}

android {
    namespace = "com.cairosquad.viewmodel"
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
    kotlinOptions {
        jvmTarget = "11"
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
    implementation(libs.androidx.core.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    testImplementation(libs.junit.jupiter)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    // test
    androidTestImplementation(libs.androidx.core.testing)
    androidTestImplementation(libs.androidx.junit.ktx)
    testImplementation(libs.kotlinx.coroutines.test)

    // google truth
    testImplementation(libs.truth)
    androidTestImplementation(libs.truth)

    // mockk
    testImplementation(libs.mockk)
    androidTestImplementation(libs.mockk)

    implementation(project(":domain"))
    testImplementation(kotlin("test"))
}