plugins {
    id("com.android.library")
    kotlin("android")
    `maven-publish`
}

android {
    compileSdk = Dep.compileSdk

    defaultConfig {
        minSdk = Dep.minSdk
        targetSdk = Dep.targetSdk

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

    packagingOptions {
        resources.excludes.addAll(
            listOf(
                "META-INF/AL2.0",
                "META-INF/LGPL2.1"
            )
        )
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
    implementation(Dep.Kotlin.stdlib)
    implementation(Dep.Kotlin.coroutine)

    implementation(Dep.Compose.ui)
    implementation(Dep.Compose.uiTooling)

    implementation(Dep.Compose.foundation)

    implementation(Dep.Compose.material)

    implementation(Dep.Compose.activity)
    implementation(Dep.Compose.runtime)

    testImplementation(Dep.Test.junit)

    androidTestImplementation(Dep.Compose.activity)
    androidTestImplementation(Dep.AndroidTest.junitExt)
    androidTestImplementation(Dep.AndroidTest.espresso)
    androidTestImplementation(Dep.AndroidTest.compose)
    debugImplementation(Dep.AndroidTest.composeManifest)
}

val sourcesJar by tasks.creating(Jar::class) {
    group = JavaBasePlugin.DOCUMENTATION_GROUP
    description = "Assembles sources JAR"
    archiveClassifier.set("sources")
    from(android.sourceSets.getByName("main").java.srcDirs)
}

artifacts {
    archives(sourcesJar)
}

afterEvaluate {
    publishing {
        repositories {
            maven {
                url = uri("$buildDir/repository")
            }
        }
        publications {
            create<MavenPublication>("release") {
                from(components["release"])
                groupId = "com.github.lhoyong"
                artifactId = "swiper"
                version = "1.0.0"
                artifact(sourcesJar)
            }
        }
    }
}
