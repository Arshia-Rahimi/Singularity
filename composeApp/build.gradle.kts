@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
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
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.ui)
                implementation(compose.components.resources)
                implementation(compose.components.uiToolingPreview)
                implementation(libs.androidx.lifecycle.viewmodelCompose)
                implementation(libs.androidx.lifecycle.runtimeCompose)
                implementation(libs.koin.compose)
                implementation(libs.koin.compose.viewmodel)
                implementation(libs.koin.compose.navigation)
                implementation(libs.datastore.preferences)
                implementation(libs.sqldelight.coroutines)
                implementation(libs.ktor.client.cio)
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.client.auth)
                implementation(libs.ktor.client.websockets)
                implementation(libs.ktor.server.cio)
                implementation(libs.ktor.server.core)
                implementation(libs.ktor.server.content.negotiation)
                implementation(libs.ktor.server.auth)
                implementation(libs.ktor.server.websockets)
                implementation(libs.ktor.json)
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.koin.core)
                implementation(libs.jwt.kt)
                implementation(libs.jwt.kt.hmac)
                api(libs.dns.sd)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }
        val androidInstrumentedTest by getting {
            dependencies {
                implementation(libs.androidx.testExt.junit)
                implementation(libs.androidx.espresso.core)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(compose.preview)
                implementation(libs.androidx.activity.compose)
                implementation(libs.koin.android.startup)
                implementation(libs.sqldelight.driver.android)
                implementation(libs.sqldelight.driver.android)
                implementation(libs.junit)
            }
        }
        val iosMain by getting {
            dependencies {
                implementation(libs.sqldelight.driver.native)
            }
        }
        val desktopMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation(libs.kotlinx.coroutinesSwing)
                implementation(libs.appdirs)
                implementation(libs.sqldelight.driver.sqlite)
                implementation(libs.jmdns)
            }
        }
    }

    compilerOptions {
        freeCompilerArgs.set(listOf("-Xwhen-guards", "-Xcontext-parameters"))
    }
}

android {
    namespace = "com.github.singularity"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.github.singularity"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
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
    debugImplementation(compose.uiTooling)
    coreLibraryDesugaring(libs.desugaring)
    testImplementation(libs.junit)
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
