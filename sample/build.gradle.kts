plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
}

android {
    compileSdk = 31

    defaultConfig {
        applicationId = "com.github.lhoyong.beautiful.sample"
        minSdk = 23
        targetSdk = 31
        versionCode = 1
        versionName = "1.0.0"

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
        kotlinCompilerExtensionVersion = "1.1.0-beta04"
    }
}

dependencies {
    implementation(project(":beautiful"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.6.0")
    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.appcompat:appcompat:1.4.0")
    implementation("com.google.android.material:material:1.4.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")

    implementation("androidx.compose.ui:ui:1.1.0-beta04")
    implementation("androidx.compose.ui:ui-tooling:1.1.0-beta04")
    implementation("androidx.compose.foundation:foundation:1.1.0-beta04")
    implementation("androidx.compose.material:material:1.1.0-beta04")
    // Material design icons
    implementation("androidx.compose.material:material-icons-core:1.1.0-beta04")
    implementation("androidx.compose.material:material-icons-extended:1.1.0-beta04")
    // Integration with activities
    implementation("androidx.activity:activity-compose:1.4.0")
    // Integration with ViewModels
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.4.0")
    // Integration with observables
    implementation("androidx.compose.runtime:runtime:1.1.0-beta04")
    implementation("androidx.compose.runtime:runtime-livedata:1.1.0-beta04")

    // UI Tests
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.1.0-beta04")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.2")

    implementation("com.google.accompanist:accompanist-insets:0.10.0")

    implementation("io.coil-kt:coil-compose:1.4.0")
}
