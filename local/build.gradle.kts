plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.androidx.room)
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
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
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
    //Project Module
    implementation(projects.repository)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.junit.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)

    // Room
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

    //test
    testImplementation(libs.mockk)
    testImplementation(libs.truth)
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.androidx.junit)
    testImplementation(libs.androidx.room.testing)
    testImplementation(libs.androidx.core.testing)
    androidTestImplementation(libs.androidx.room.testing)
    androidTestImplementation(libs.androidx.runner)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.androidx.core.testing)
    androidTestImplementation(libs.mockk)
    androidTestImplementation(libs.truth)


    implementation(project(":repository"))
    testImplementation(kotlin("test"))
}