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
    id("org.jetbrains.kotlinx.kover") version "0.9.1"
}

kover {
    reports {
        filters {
            excludes {
                classes(
                    "**.model.**",
                    "**.di.**",
                    "**.exception.**",
                    "**.mapper.**",
                    "**.ui.**",
                    "**.design_system.**",
                    "**.entity.**",
                )
            }
        }

        total {
            verify {
                rule {
                    minBound(0) //temporary until adding the real tests
                }
            }
        }
    }
}

dependencies {
    kover(project(":remote"))
    kover(project(":usecase"))
    kover(project(":repository"))
    kover(project(":viewmodel"))
    kover(project(":safe_image_viewer"))
}