import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    // kmp
    alias(libs.plugins.kotlin.multiplatform)
    // cmp
    alias(libs.plugins.compose.multiplatform)
    // compose-compiler
    alias(libs.plugins.compose.compiler)
    // android-kmp-library
	alias(libs.plugins.android.kmp.library)
    // serialization
    alias(libs.plugins.kotlin.serialization)
    // sqldelight
    alias(libs.plugins.sqldelight)
}

kotlin {

    applyDefaultHierarchyTemplate()

	jvmToolchain(17)

    android {
		namespace = "com.github.singularity"
		compileSdk = 36
		minSdk = 26

        androidResources {
            enable = true
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
                // compose
	            implementation(libs.compose.runtime)
	            implementation(libs.compose.foundation)
	            implementation(libs.compose.material3)
	            implementation(libs.compose.ui)
                implementation(libs.compose.ui.tooling.preview)
	            implementation(libs.compose.nav3.ui)
	            implementation(libs.compose.nav3.viewmodel)
	            implementation(libs.compose.components.resources)
                // lifecycle
                implementation(libs.androidx.lifecycle.viewmodelCompose)
                implementation(libs.androidx.lifecycle.runtimeCompose)
                // koin
                implementation(libs.koin.core)
                implementation(libs.koin.compose)
                implementation(libs.koin.compose.viewmodel)
	            implementation(libs.koin.compose.viewmodel.navigation)
                // sqldelight
                implementation(libs.sqldelight.coroutines)
                // ktor
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.cio)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.client.auth)
                implementation(libs.ktor.client.websockets)
                implementation(libs.ktor.json)
                // serialization
                implementation(libs.kotlinx.serialization.json)
                // kolor
                implementation(libs.kolor)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(libs.kotlin.test)
                implementation(libs.junit)
            }
        }

	    val desktopMain by getting {
		    dependencies {
                // compose
			    implementation(compose.desktop.currentOs)
                // swing
			    implementation(libs.kotlinx.coroutinesSwing)
                // appDirs
			    implementation(libs.appdirs)
                // sqldelight
			    implementation(libs.sqldelight.driver.sqlite)
                // ktor
			    implementation(libs.ktor.server.cio)
			    implementation(libs.ktor.server.core)
			    implementation(libs.ktor.server.content.negotiation)
			    implementation(libs.ktor.server.auth)
			    implementation(libs.ktor.server.websockets)
                // zeroconf
			    implementation(files("libs/desktop/zeroconf-1.0.3.jar"))
		    }
	    }

	    val mobileMain by creating {
		    dependsOn(commonMain)
		    dependencies {
                // dns-sd
			    implementation(libs.dns.sd)
		    }
	    }

	    val androidMain by getting {
		    dependsOn(mobileMain)
		    dependencies {
                // sqldelight
			    implementation(libs.sqldelight.driver.android)
                // compose
                implementation(libs.compose.ui.tooling)
		    }
	    }

        val iosMain by getting {
		    dependsOn(mobileMain)
		    dependencies {
                // sqldelight
			    implementation(libs.sqldelight.driver.native)
		    }
	    }

    }

    compilerOptions {
	    freeCompilerArgs.set(
		    listOf(
			    "-Xwhen-guards",
			    "-Xcontext-parameters",
		    )
	    )
    }
}

dependencies {
    "androidRuntimeClasspath"(libs.compose.ui.tooling)
}

compose.desktop {
    application {
        mainClass = "com.github.singularity.app.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.AppImage)
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
