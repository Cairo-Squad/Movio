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
                    minBound(80)
                }
            }
        }
    }
}

dependencies {
    kover(project(":remote"))
    "kover"(project(":domain"))
    kover(project(":repository"))
    kover(project(":viewmodel"))
}