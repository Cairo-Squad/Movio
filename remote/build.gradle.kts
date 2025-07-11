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
}
dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(project(":repository"))

    implementation(libs.bundles.ktor)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.datastore)

    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.runtime)


    testImplementation(kotlin("test"))

    testImplementation(libs.ktor.client.mock)

    testImplementation(libs.kotlinx.coroutines.test.v1102)

    testImplementation(libs.junit.jupiter)
    testImplementation(libs.junit.junit)
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${libs.versions.junitJupiter.get()}")
    testImplementation(libs.truth.v144)

    testImplementation(libs.koin.test)

}