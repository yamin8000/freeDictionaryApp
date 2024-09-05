plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.google.ksp)
    alias(libs.plugins.jetbrains.compose.plugin)
    id("org.jetbrains.kotlin.kapt")
    alias(libs.plugins.hilt)
}

android {
    namespace = "io.github.yamin8000.owl.search"
    compileSdk = 34

    defaultConfig {
        minSdk = 21
        ksp.arg("room.schemaLocation", "$projectDir/schemas")
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
    implementation(project(":common"))
    implementation(project(":strings"))
    implementation(project(":datastore"))
    //core android/kotlin
    coreLibraryDesugaring(libs.desugar.jdk.libs)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.jetbrains.kotlinx.immutable)
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
    //hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose)
    //room
    implementation(libs.androidx.room.runtime)
    annotationProcessor(libs.androidx.room.compiler)
    ksp(libs.androidx.room.compiler)
    api(libs.androidx.room.ktx)
    //retrofit
    api(libs.retrofit.main)
    implementation(libs.retrofit.converter.moshi)
    kapt(libs.retrofit.type.keeper)
    //moshi
    implementation(libs.moshi.kotlin)
    ksp(libs.moshi.kotlin.codegen)
}