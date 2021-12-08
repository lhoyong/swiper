plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
}

android {
    compileSdk = Dep.compileSdk

    defaultConfig {
        applicationId = "com.github.lhoyong.beautiful.sample"
        minSdk = Dep.minSdk
        targetSdk = Dep.targetSdk
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
        kotlinCompilerExtensionVersion = Dep.Compose.version
    }
}

dependencies {
    implementation(project(":beautiful"))
    implementation(Dep.Kotlin.stdlib)
    implementation(Dep.Kotlin.coroutine)

    implementation(Dep.Android.core)
    implementation(Dep.Android.material)

    implementation(Dep.Compose.ui)
    implementation(Dep.Compose.uiTooling)

    implementation(Dep.Compose.foundation)

    implementation(Dep.Compose.material)
    implementation(Dep.Compose.materialIcon)
    implementation(Dep.Compose.materialIconsExtended)

    implementation(Dep.Compose.activity)
    implementation(Dep.Compose.runtime)

    implementation(Dep.Compose.accompanist)

    testImplementation(Dep.Test.junit)

    androidTestImplementation(Dep.AndroidTest.junitExt)
    androidTestImplementation(Dep.AndroidTest.espresso)
    androidTestImplementation(Dep.AndroidTest.compose)

    implementation(Dep.coil)
}
