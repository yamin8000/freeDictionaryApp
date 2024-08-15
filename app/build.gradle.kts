/*
 *     freeDictionaryApp/freeDictionaryApp.app
 *     build.gradle.kts Copyrighted by Yamin Siahmargooei at 2024/5/9
 *     build.gradle.kts Last modified at 2024/5/5
 *     This file is part of freeDictionaryApp/freeDictionaryApp.app.
 *     Copyright (C) 2024  Yamin Siahmargooei
 *
 *     freeDictionaryApp/freeDictionaryApp.app is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     freeDictionaryApp/freeDictionaryApp.app is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with freeDictionaryApp.  If not, see <https://www.gnu.org/licenses/>.
 */

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.plugin)
    alias(libs.plugins.kotlin.parcelize)
}

private val appId = "io.github.yamin8000.owl"

android {
    namespace = appId
    compileSdk = 34

    defaultConfig {
        applicationId = appId
        minSdk = 21
        targetSdk = 34
        versionCode = 42
        versionName = "1.6.5"
        vectorDrawables.useSupportLibrary = true
        base.archivesName = "$applicationId-v$versionCode-n$versionName"
    }

    buildTypes {
        release {
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            isMinifyEnabled = true
            isShrinkResources = true
        }
        debug {
            isMinifyEnabled = false
            isShrinkResources = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
        languageVersion = "1.9"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    kotlin.sourceSets.configureEach {
        languageSettings.enableLanguageFeature("DataObjects")
    }
}

dependencies {
    //modules
    implementation(project(":data"))
    implementation(project(":network"))
    //core android/kotlin
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.androidx.core.splashscreen)
    //compose
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.compose.ui.tooling.preview)
    debugImplementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material3.window.size)
    implementation(libs.androidx.lifecycle.runtime.compose)
    //coil
    val coilVersion = "2.7.0"
    implementation("io.coil-kt:coil:$coilVersion")
    implementation("io.coil-kt:coil-compose:$coilVersion")
    //navigation
    implementation(libs.androidx.navigation.compose)
    //lottie
    implementation("com.airbnb.android:lottie-compose:6.5.0")
}