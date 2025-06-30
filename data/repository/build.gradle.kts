plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
    jacoco                                           // ↙️ فعّلي Jacoco
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
    }
}

dependencies {
    implementation(project(":domain:usecase"))

    // 🧪 Unit‑test deps
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
}

jacoco {
    toolVersion = "0.8.10"
}

/** ❶ شغّلي JUnit 5 */
tasks.test {
    useJUnitPlatform()
    finalizedBy("jacocoTestReport")               // يولِّد التقرير بعد التستات
}

/** ❷ عدّلي التاسك الجاهز بدل ما تعيدي تعريفه */
tasks.named<JacocoReport>("jacocoTestReport") {

    dependsOn(tasks.test)

    // 📝 شكل التقارير
    reports {
        xml.required.set(true)                   // نحتاجه للـ CI
        html.required.set(true)                  // للقراءة المحلية
    }

    // استبعد الملفات اللي ملهاش لازمة في الكڤرج
    val fileFilter = listOf(
        "**/R.class", "**/BuildConfig.*", "**/*Test*.*"
    )

    /** أين توجد الـ .class */
    classDirectories.setFrom(
        files(
            fileTree("${buildDir}/classes/kotlin/main") {
                exclude(fileFilter)
            }
        )
    )

    /** السورس */
    sourceDirectories.setFrom(files("src/main/java", "src/main/kotlin"))

    /** ملفات الـ exec */
    executionData.setFrom(
        fileTree(buildDir) { include("jacoco/test.exec") }
    )
}
