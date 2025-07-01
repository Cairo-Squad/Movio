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
                project.tasks.matching { it.name == "jacocoTestReport" }
                    .firstOrNull()
                    ?.let { finalizedBy(it) }
            }
        }


        tasks.register<JacocoReport>("jacocoMergedReport") {
            group = "verification"
            description = "Aggregated JaCoCo coverage for selected modules."

             val coveredModules = setOf("repository", "remote", "usecase", "viewmodel")

            val fileFilter = listOf("**/R.class", "**/BuildConfig.*", "**/*Test*.*")


            val includedProjects = subprojects.filter { it.path in coveredModules }

            val subprojectReports = includedProjects.mapNotNull { it.tasks.findByName("jacocoTestReport") }
            dependsOn(subprojectReports)

            classDirectories.setFrom(
                includedProjects.map { project ->
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
                includedProjects.map { project ->
                    files(
                        "${project.projectDir}/src/main/java",
                        "${project.projectDir}/src/main/kotlin"
                    )
                }
            )

            executionData.setFrom(
                includedProjects.map { project ->
                    fileTree(project.buildDir) {
                        include(
                            "jacoco/test.exec",
                            "jacoco/test*.exec",
                            "jacoco/testDebugUnitTest.exec"
                        )
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
