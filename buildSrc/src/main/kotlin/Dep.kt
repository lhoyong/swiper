object Dep {

    const val minSdk = 23
    const val compileSdk = 31
    const val targetSdk = 31

    const val versionCode = 1
    const val versionName = "1.0.0"


    object Plugins {
        const val androidGradlePlugin = "com.android.tools.build:gradle:7.0.3"
        const val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Kotlin.version}"
        const val spotless = "com.diffplug.spotless:spotless-plugin-gradle:5.15.0"
    }

    object Kotlin {
        const val version = "1.6.0"
        const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib:$version"
        const val coroutine = "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2"
    }

    object Android {
        const val core = "androidx.core:core-ktx:1.7.0"
        const val appcompat = "androidx.appcompat:appcompat:1.3.1"

        const val material = "com.google.android.material:material:1.4.0"
        const val constraintLayout = "androidx.constraintlayout:constraintlayout:2.1.1"
    }

    object Compose {
        const val version = "1.1.0-beta04"

        const val runtime = "androidx.compose.runtime:runtime:$version"

        const val material = "androidx.compose.material:material:$version"
        const val materialIcon = "androidx.compose.material:material-icons-core:$version"
        const val materialIconsExtended =
            "androidx.compose.material:material-icons-extended:$version"

        const val ui = "androidx.compose.ui:ui:$version"
        const val uiTooling = "androidx.compose.ui:ui-tooling:$version"
        const val foundation = "androidx.compose.foundation:foundation:$version"

        const val activity = "androidx.activity:activity-compose:1.4.0"

        const val constraintLayout = "androidx.constraintlayout:constraintlayout-compose:1.0.0-rc01"

        const val accompanist = "com.google.accompanist:accompanist-insets:0.10.0"
    }

    object Test {
        const val junit = "junit:junit:4.13.2"
    }

    object AndroidTest {
        const val junitExt = "androidx.test.ext:junit:1.1.3"
        const val espresso = "androidx.test.espresso:espresso-core:3.4.0"
        const val compose = "androidx.compose.ui:ui-test-junit4:${Compose.version}"
    }

    const val coil = "io.coil-kt:coil-compose:1.4.0"
}