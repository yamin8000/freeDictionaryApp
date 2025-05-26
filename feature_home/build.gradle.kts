plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.google.ksp)
    alias(libs.plugins.jetbrains.compose.plugin)
    alias(libs.plugins.hilt)
}

android {
    namespace = "io.github.yamin8000.owl.feature_home"
    compileSdk = 35

    defaultConfig {
        minSdk = 21
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    //core
    coreLibraryDesugaring(libs.desugar.jdk.libs)
    implementation(project(":common"))
    implementation(project(":search"))
    //hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose)
}