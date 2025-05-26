plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.google.ksp)
    alias(libs.plugins.jetbrains.compose.plugin)
    alias(libs.plugins.hilt)
}

android {
    namespace = "io.github.yamin8000.owl.core"
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
    api(project(":strings"))
    api(project(":datastore"))
    //core android/kotlin
    coreLibraryDesugaring(libs.desugar.jdk.libs)
    api(libs.androidx.core.ktx)
    api(libs.androidx.lifecycle.viewmodel.compose)
    //compose
    api(libs.androidx.compose.ui)
    api(libs.androidx.compose.material)
    api(libs.androidx.compose.ui.tooling.preview)
    debugApi(libs.androidx.compose.ui.tooling)
    api(libs.androidx.activity.compose)
    api(libs.androidx.compose.material.icons.extended)
    api(libs.androidx.compose.material3)
    api(libs.androidx.compose.material3.window.size)
    api(libs.androidx.lifecycle.runtime.compose)
    api(platform(libs.androidx.compose.bom))
    androidTestApi(platform(libs.androidx.compose.bom))
    //hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose)
    //lottie
    implementation(libs.lottie.compose)
}