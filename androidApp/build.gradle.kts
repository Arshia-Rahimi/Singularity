plugins {
	// android
	alias(libs.plugins.android.application)
	// compose-compiler
	alias(libs.plugins.compose.compiler)
}

android {
	namespace = "com.github.android.singularity"
	compileSdk = 36

	defaultConfig {
		applicationId = "com.github.singularity"
		minSdk = 26
		targetSdk = 36
		versionCode = 1
		versionName = "1.0"
	}

	buildTypes {
		release {
			isMinifyEnabled = true
			isDebuggable = false
		}

		debug {
			isMinifyEnabled = false
			isDebuggable = true
		}
	}

    compileOptions {
		sourceCompatibility = JavaVersion.VERSION_17
		targetCompatibility = JavaVersion.VERSION_17
	}

	buildFeatures {
		compose = true
	}
}

dependencies {
    implementation(projects.composeApp)

    // compose
    implementation(libs.androidx.activity.compose)
    // koin
    implementation(libs.koin.android)
}
