import com.android.build.api.dsl.androidLibrary
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
	alias(libs.plugins.android.kmp)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.sqldelight)
}

kotlin {

    applyDefaultHierarchyTemplate()

	jvmToolchain(17)

	androidLibrary {
		namespace = "com.github.singularity"
		compileSdk = 36
		minSdk = 26

		localDependencySelection {
			selectBuildTypeFrom.set(listOf("debug", "release"))
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
			    implementation(compose.desktop.currentOs)
			    implementation(libs.kotlinx.coroutinesSwing)
			    implementation(libs.appdirs)
			    implementation(libs.sqldelight.driver.sqlite)
			    implementation(libs.ktor.server.cio)
			    implementation(libs.ktor.server.core)
			    implementation(libs.ktor.server.content.negotiation)
			    implementation(libs.ktor.server.auth)
			    implementation(libs.ktor.server.websockets)
			    implementation(files("libs/desktop/zeroconf-1.0.2.jar"))
		    }
	    }

	    val mobileMain by creating {
		    dependsOn(commonMain)
		    dependencies {
			    implementation(libs.dns.sd)
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
		    )
	    )
    }
}

dependencies {
	"androidRuntimeClasspath"(libs.androidx.compose.ui.tooling)
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
