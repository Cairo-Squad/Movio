import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
	alias(libs.plugins.android.library)
	alias(libs.plugins.kotlin.android)
	alias(libs.plugins.kotlin.compose)
	alias(libs.plugins.kover)
}

android {
	namespace = "com.cairosquad.safe_image_viewer"
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
		sourceCompatibility = JavaVersion.VERSION_11
		targetCompatibility = JavaVersion.VERSION_11
	}
	kotlin {
		compilerOptions {
			jvmTarget.set(JvmTarget.JVM_11)
		}
	}
	packaging {
		resources {
			excludes += arrayOf(
				"/META-INF/{AL2.0,LGPL2.1}",
				"META-INF/LICENSE.md",
				"META-INF/LICENSE",
				"META-INF/NOTICE",
				"META-INF/LICENSE-notice",
				"META-INF/LICENSE-notice.md"
			)
		}
	}
	buildFeatures {
		compose = true
	}
}

dependencies {

	implementation(libs.core.ktx)
	implementation(libs.coil.compose)
	implementation(libs.test.junit.ktx)
	testImplementation(kotlin("test"))
	testImplementation(libs.junit.jupiter)
	testImplementation(libs.mockk)
	testImplementation(libs.truth)
	androidTestImplementation(libs.mockk)
	androidTestImplementation(libs.truth)
	testImplementation(libs.junit)
	testImplementation(libs.coroutines.test)
	implementation(libs.coil.okhttp)
	api("com.google.firebase:firebase-ml-vision:24.1.0") {
		exclude( group="com.google.android.gms", module="play-services-vision-common")
	}
	implementation(libs.firebase.ml.vision.automl)
	implementation(libs.firebase.ml.model.interpreter)
}

kover {
	reports {
		filters {
			excludes {
				classes(
					"**.alghorithm.**",
					"**.loader.**",
					"**.modifier.**",
					"**.FastBlurAlgorithmKt",
				)
			}
		}
	}
}