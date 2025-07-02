// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
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
                    minBound(80)
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
}