plugins {
	alias(libs.plugins.android.application)
	alias(libs.plugins.kotlin.android)
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
}

dependencies {
    implementation(projects.composeApp)
}
