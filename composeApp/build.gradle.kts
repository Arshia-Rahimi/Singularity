import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.sqldelight)
}

kotlin {
    applyDefaultHierarchyTemplate()

    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    jvm("desktop")

    sourceSets {
        val commonMain by getting {
            dependencies {
	            implementation(libs.compose.runtime)
	            implementation(libs.compose.foundation)
	            implementation(libs.compose.material3)
	            implementation(libs.compose.ui)
	            implementation(libs.compose.nav3.ui)
	            implementation(libs.compose.nav3.viewmodel)
	            implementation(libs.compose.components.resources)
                implementation(libs.androidx.lifecycle.viewmodelCompose)
                implementation(libs.androidx.lifecycle.runtimeCompose)
                implementation(libs.koin.core)
                implementation(libs.koin.compose)
                implementation(libs.koin.compose.viewmodel)
	            implementation(libs.koin.compose.viewmodel.navigation)
                implementation(libs.sqldelight.coroutines)
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.cio)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.client.auth)
                implementation(libs.ktor.client.websockets)
                implementation(libs.ktor.json)
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.kolor)
                api(libs.dns.sd)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(libs.kotlin.test)
                implementation(libs.junit)
            }
        }

	    val mobileMain by creating {
		    dependsOn(commonMain)
	    }

	    val desktopMain by getting {
		    dependencies {
			    implementation(compose.desktop.currentOs)
			    implementation(libs.kotlinx.coroutinesSwing)
			    implementation(libs.appdirs)
			    implementation(libs.sqldelight.driver.sqlite)
			    implementation(libs.ktor.server.cio)
			    implementation(libs.ktor.server.core)
			    implementation(libs.ktor.server.content.negotiation)
			    implementation(libs.ktor.server.auth)
			    implementation(libs.ktor.server.websockets)
			    implementation(libs.ktor.server.tls)
			    implementation(files("libs/desktop/zeroconf-1.0.2.jar"))
		    }
	    }

	    val androidInstrumentedTest by getting {
		    dependencies {
			    implementation(libs.androidx.testExt.junit)
			    implementation(libs.androidx.espresso.core)
		    }
	    }
	    val androidMain by getting {
		    dependsOn(mobileMain)
		    dependencies {
			    implementation(libs.androidx.activity.compose)
			    implementation(libs.sqldelight.driver.android)
		    }
	    }
	    val iosMain by getting {
		    dependsOn(mobileMain)
		    dependencies {
			    implementation(libs.sqldelight.driver.native)
		    }
	    }

    }

    compilerOptions {
	    freeCompilerArgs.set(
		    listOf(
			    "-Xwhen-guards",
			    "-Xcontext-parameters",
			    "-Xexpect-actual-classes"
		    )
	    )
    }
}

android {
    namespace = "com.github.singularity"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.github.singularity"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
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
	    isCoreLibraryDesugaringEnabled = true
    }

    testOptions {
        unitTests.isIncludeAndroidResources = true
    }
}

dependencies {
	debugImplementation(libs.compose.ui.tooling)
    testImplementation(libs.junit)
	coreLibraryDesugaring(libs.desugaring)
}

compose.desktop {
    application {
        mainClass = "com.github.singularity.app.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.github.singularity"
            packageVersion = "1.0.0"
        }
    }
}

sqldelight {
    databases {
        create("SingularityDatabase") {
            packageName.set("com.github.singularity.core.database")
        }
    }
}
