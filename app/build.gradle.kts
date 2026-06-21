plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
}

android {
    namespace = "mx.utng.cfga.utngrunner"
    compileSdk = 35

    defaultConfig {
        applicationId = "mx.utng.cfga.utngrunner"
        minSdk = 30
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    // 1. Core de Android y Ciclos de Vida
    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.activity.compose)
    implementation(libs.core.splashscreen)

    // 2. Jetpack Compose para Wear OS (Esencial para las interfaces del juego)
    implementation(platform(libs.compose.bom))
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.compose.foundation)

    // 🚨 DEPENDENCIAS CRÍTICAS PARA WEAR OS
    implementation(libs.wear.compose.material)
    implementation(libs.wear.compose.navigation)

    // 3. Soporte para la Corona Giratoria (Rotary Input)
    implementation(libs.horologist.compose.layout)

    // 4. Base de Datos Local (Room) para persistencia de puntuaciones (Clean Architecture)
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    // 5. Servicios de Google Play para Relojes
    implementation(libs.play.services.wearable)

    // 6. Entorno de Pruebas y Debugging
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)
    implementation(libs.wear.tooling.preview)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.ui.test.junit4)
}
