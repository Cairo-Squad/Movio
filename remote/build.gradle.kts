plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kover)}
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
    implementation(project(":repository"))
    testImplementation(kotlin("test"))
    testImplementation(libs.junit.jupiter)
    // Ktor
    implementation(libs.bundles.ktor)
    implementation(libs.logging.interceptor)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.logging.interceptor)
    // Data Store
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.datastore)
    // Serialization
    implementation(libs.kotlinx.serialization.json)
}