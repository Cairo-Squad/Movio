// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
    alias(libs.plugins.google.gms.google.services) apply false
    alias(libs.plugins.google.firebase.crashlytics) apply false
    alias(libs.plugins.google.firebase.perf) apply false
    alias(libs.plugins.google.firebase.appdistribution) apply false
    alias(libs.plugins.kover)
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.ksp) apply false
}

buildscript {
    dependencies {
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.51")
    }
}

kover {
    reports {
        filters {
            excludes {
                classes(
                    "**.model.**",
                    "**.di.**",
                    "**.exception.**",
                    "**.exceptions.**",
                    "**.mapper.**",
                    "**.ui.**",
                    "**.design_system.**",
                    "**.entity.**",
                    "**.local.**",
                    "**.safe_image_viewer.modifier.**",
                    "**.safe_image_viewer.alghorithm.**",
                    "**.safe_image_viewer.safe_image_viewer.**",
                    "**.safe_image_viewer.loader.**",
                )
            }
        }
        total {
            verify {
                rule {
                    minBound(80)
                }
            }
        }
    }
}

dependencies {
    kover(project(":remote"))
    kover(project(":domain"))
    kover(project(":repository"))
    kover(project(":viewmodel"))
    kover(project(":safe_image_viewer"))
}
configurations.all {
    resolutionStrategy {
        force("androidx.lifecycle:lifecycle-runtime-ktx:2.8.0")
        force("androidx.lifecycle:lifecycle-runtime:2.8.0")
        force("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.0")
        force("androidx.lifecycle:lifecycle-runtime-compose:2.8.0")
    }
}