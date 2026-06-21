plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
}

android {
    namespace = "mx.utng.cfga.utngrunner"
    compileSdk = 36

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

    // 2. Jetpack Compose para Wear OS
    implementation(platform(libs.compose.bom))
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)

    // 🚨 DEPENDENCIAS WEAR OS
    implementation(libs.wear.compose.material)
    implementation(libs.wear.compose.material3)
    implementation(libs.wear.compose.foundation)
    implementation(libs.wear.compose.navigation)
    implementation(libs.wear.tooling.preview)
    debugImplementation(libs.wear.compose.ui.tooling)

    // 3. Soporte para la Corona Giratoria (Rotary Input)
    implementation(libs.horologist.compose.layout)

    // 4. Persistencia y Datos
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)
    implementation(libs.datastore.preferences)

    // 5. Servicios de Salud y Wearable
    implementation(libs.play.services.wearable)
    implementation(libs.health.services.client)

    // 6. Pruebas y Debugging
    testImplementation(libs.junit)
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.ui.test.junit4)
}
