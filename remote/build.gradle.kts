import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kover)
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
    properties.load(rootProject.file("secret.properties").inputStream())
    properties.getProperty("API_KEY")

    buildTypes {
        release {
            buildConfigField("String", "API_KEY", properties.getProperty("API_KEY"))
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            buildConfigField("String", "API_KEY", properties.getProperty("API_KEY"))
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
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.8"
    }
}
dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(project(":repository"))
    testImplementation(kotlin("test"))
    testImplementation(libs.junit.jupiter)
    // Ktor
    implementation(libs.bundles.ktor)
    implementation(libs.logging.interceptor)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.logging.interceptor)
    // Data Store
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.datastore)
    // Serialization
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.runtime)

}