import org.jetbrains.kotlin.gradle.plugin.mpp.pm20.util.archivesName

/*
 *     freeDictionaryApp is a simple android application for freeDictionaryAPI
 *     build.gradle Created by Yamin Siahmargooei at 2022/6/16
 *     This file is part of freeDictionaryAPI.
 *     Copyright (C) 2022  Yamin Siahmargooei
 *
 *     freeDictionaryApp is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     freeDictionaryApp is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with freeDictionaryAPI.  If not, see <https://www.gnu.org/licenses/>.
 */

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
    id("com.google.devtools.ksp") version ("1.9.22-1.0.17")
}

private val composeCompilerVersion = "1.5.8"

private val appId = "io.github.yamin8000.owl"

android {
    namespace = appId
    compileSdk = 34

    defaultConfig {
        applicationId = appId
        minSdk = 21
        targetSdk = 34
        versionCode = 37
        versionName = "1.6.0"
        vectorDrawables.useSupportLibrary = true
        ksp.arg("room.schemaLocation", "$projectDir/schemas")
        archivesName = "$applicationId-v$versionCode($versionName)"
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

    composeOptions {
        kotlinCompilerExtensionVersion = composeCompilerVersion
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
    //core android
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
    //compose
    val material3Version = "1.2.0"
    val composeLibsVersion = "1.6.1"
    val composeUiLibsVersion = "1.6.1"
    implementation("androidx.compose.ui:ui:$composeUiLibsVersion")
    implementation("androidx.compose.material:material:$composeLibsVersion")
    implementation("androidx.compose.ui:ui-tooling-preview:$composeUiLibsVersion")
    debugImplementation("androidx.compose.ui:ui-tooling:$composeUiLibsVersion")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation("androidx.compose.material:material-icons-extended:$composeLibsVersion")
    implementation("androidx.compose.material3:material3:$material3Version")
    implementation("androidx.compose.material3:material3-window-size-class:$material3Version")
    //network
    val retrofitVersion = "2.9.0"
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-moshi:$retrofitVersion")
    //coil
    val coilVersion = "2.5.0"
    implementation("io.coil-kt:coil:$coilVersion")
    implementation("io.coil-kt:coil-compose:$coilVersion")
    //navigation
    val navVersion = "2.7.6"
    implementation("androidx.navigation:navigation-compose:$navVersion")
    //datastore
    implementation("androidx.datastore:datastore-preferences:1.0.0")
    //room
    val roomVersion = "2.6.1"
    implementation("androidx.room:room-runtime:$roomVersion")
    annotationProcessor("androidx.room:room-compiler:$roomVersion")
    ksp("androidx.room:room-compiler:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    //lottie
    implementation("com.airbnb.android:lottie-compose:6.3.0")
    //
    implementation("org.jetbrains.kotlinx:kotlinx-collections-immutable:0.3.7")
}