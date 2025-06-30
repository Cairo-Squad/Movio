// Top-level build file where you can add configuration options common to all sub-projects/modules.
import org.gradle.api.tasks.testing.Test

        plugins {
            alias(libs.plugins.android.application) apply false
            alias(libs.plugins.kotlin.android) apply false
            alias(libs.plugins.kotlin.compose) apply false
            alias(libs.plugins.android.library) apply false
            alias(libs.plugins.jetbrains.kotlin.jvm) apply false
        }

subprojects {
    apply(plugin = "jacoco")

    tasks.withType<Test>().configureEach {
        useJUnitPlatform()
        finalizedBy(tasks.named("jacocoTestReport"))
    }
}

tasks.register<JacocoReport>("jacocoMergedReport") {
    group = "verification"
    description = "Generates an aggregated JaCoCo coverage report from all subprojects."

    val subprojectReports = subprojects.map { it.tasks.named<JacocoReport>("jacocoTestReport") }
    dependsOn(subprojectReports)

    val fileFilter = listOf(
        "**/R.class", "**/BuildConfig.*", "**/*Test*.*"
    )

    classDirectories.setFrom(
        subprojects.map { project ->
            files(
                fileTree("${project.buildDir}/classes/kotlin/main") {
                    exclude(fileFilter)
                },
                fileTree("${project.buildDir}/tmp/kotlin-classes/debug") {
                    exclude(fileFilter)
                }
            )
        }
    )

    sourceDirectories.setFrom(
        subprojects.map { project ->
            files(
                "${project.projectDir}/src/main/java",
                "${project.projectDir}/src/main/kotlin",
                "${project.projectDir}/src/debug/java",
                "${project.projectDir}/src/debug/kotlin"
            )
        }
    )

    executionData.setFrom(
        subprojects.map { project ->
            fileTree(project.buildDir) {
                include("jacoco/test.exec", "jacoco/test*.exec")
            }
        }
    )

    reports {
        xml.required.set(true)
        html.required.set(true)
        xml.outputLocation.set(file("$buildDir/reports/jacoco/merged/jacocoTestReport.xml"))
        html.outputLocation.set(file("$buildDir/reports/jacoco/merged/html"))
    }
}