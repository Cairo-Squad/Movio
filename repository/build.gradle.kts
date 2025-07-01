plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
    jacoco
}
java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17
    }
}
dependencies {
    implementation(project(":usecase"))
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
}

jacoco {
    toolVersion = "0.8.10"
}

tasks.test {
    useJUnitPlatform()
    finalizedBy("jacocoTestReport")
}

tasks.named<JacocoReport>("jacocoTestReport") {
    dependsOn(tasks.test)

    reports {
        xml.required.set(true)
        html.required.set(true)
    }

    val fileFilter = listOf("**/R.class", "**/BuildConfig.*", "**/*Test*.*")

    classDirectories.setFrom(
        files(fileTree("${buildDir}/classes/kotlin/main") {
            exclude(fileFilter)
        })
    )

    sourceDirectories.setFrom(files("src/main/java", "src/main/kotlin"))

    executionData.setFrom(fileTree(buildDir) {
        include("jacoco/test.exec")
    })
}
