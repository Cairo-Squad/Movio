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
    kotlinOptions {
        jvmTarget = "11"
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
    implementation(projects.repository)

    implementation(platform(libs.androidx.compose.bom))

    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.datastore)

    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.runtime)
    implementation(libs.logging.interceptor)

    // --- Retrofit 3 ---
    implementation(libs.retrofit)
    implementation(libs.converter.kotlinx.serialization)
    implementation (libs.okhttp)

    implementation(libs.logging.interceptor)

    //test
    testImplementation(kotlin("test"))
    testImplementation(libs.mockwebserver)


    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.junit.junit)
    testImplementation(libs.junit.junit)
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${libs.versions.junitJupiter.get()}")
    testImplementation(libs.truth)
    testImplementation(libs.koin.test)
    testImplementation(libs.mockk)

}