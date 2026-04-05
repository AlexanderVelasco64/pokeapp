plugins {
    alias(libs.plugins.android.application)
    //id("org.jetbrains.kotlin.android")
    alias(libs.plugins.kotlin.compose)
    //alias(libs.plugins.ksp) // Added KSP via alias
    // ... other plugins
    id("com.google.devtools.ksp")
    id("androidx.room") version "2.7.0"
}

android {
    namespace = "com.example.pokeapp"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.example.pokeapp"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
}

room {
    schemaDirectory("$projectDir/schemas")
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // Retrofit for networking
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    // Converter to turn JSON into Kotlin Objects
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    // Optional: Logging for debugging network calls
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    val room_version = "2.8.4"
    ksp("androidx.room:room-compiler:$room_version")
    implementation("androidx.room:room-runtime:$room_version")
    // Optional: for Kotlin Coroutines and Flow support
    implementation("androidx.room:room-ktx:$room_version")

    implementation("io.coil-kt:coil-compose:2.6.0")

}