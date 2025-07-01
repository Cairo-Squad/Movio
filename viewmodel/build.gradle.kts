plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    jacoco
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    // test Android Studio
    testOptions {
        unitTests.isIncludeAndroidResources = true
    }
}

jacoco {
    toolVersion = "0.8.10"
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(project(":usecase"))

    testImplementation(kotlin("test"))
    testImplementation("junit:junit:4.13.2")
}

tasks.register<JacocoReport>("jacocoTestReport") {
    dependsOn("testDebugUnitTest")

    reports {
        xml.required.set(true)
        html.required.set(true)
    }

    val fileFilter = listOf("**/R.class", "**/BuildConfig.*", "**/*Test*.*")

    classDirectories.setFrom(
        fileTree("${buildDir}/intermediates/classes/debug") {
            exclude(fileFilter)
        }
    )

    sourceDirectories.setFrom(
        files(
            "src/main/java",
            "src/main/kotlin"
        )
    )

    executionData.setFrom(
        fileTree(buildDir) {
            include("jacoco/testDebugUnitTest.exec")
        }
    )
}
