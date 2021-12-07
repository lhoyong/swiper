plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
}

android {
    compileSdk = 31

    defaultConfig {
        minSdk = 23
        targetSdk = 31

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
    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.1.0-beta03"
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.5.31")
    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.appcompat:appcompat:1.4.0")
    implementation("com.google.android.material:material:1.4.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")

    implementation("androidx.compose.ui:ui:1.1.0-beta03")
    // Tooling support (Previews, etc.)
    implementation("androidx.compose.ui:ui-tooling:1.1.0-beta03")
    // Foundation (Border, Background, Box, Image, Scroll, shapes, animations, etc.)
    implementation("androidx.compose.foundation:foundation:1.1.0-beta03")
    implementation("androidx.compose.animation:animation:1.1.0-beta03")
    // Material Design
    implementation("androidx.compose.material:material:1.1.0-beta03")
    // Material design icons
    implementation("androidx.compose.material:material-icons-core:1.1.0-beta03")
    implementation("androidx.compose.material:material-icons-extended:1.1.0-beta03")
    // Integration with activities
    implementation("androidx.activity:activity-compose:1.4.0")
    // Integration with observables
    implementation("androidx.compose.runtime:runtime:1.1.0-beta03")
    implementation("androidx.compose.runtime:runtime-livedata:1.1.0-beta03")

    // UI Tests
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.1.0-beta03")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.4.3")

    implementation("com.google.accompanist:accompanist-insets:0.10.0")
}
