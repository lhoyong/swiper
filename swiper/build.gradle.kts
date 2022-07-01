import org.jetbrains.kotlin.konan.properties.Properties

plugins {
    id("com.android.library")
    kotlin("android")
    id("maven-publish")
    id("signing")
}

android {
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()

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
        create("benchmark") {}
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
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }
}

dependencies {
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlin.coroutines.core)

    implementation(libs.compose.foundation)
    implementation(libs.compose.foundation.layout)
    implementation(libs.compose.ui.util)
    implementation(libs.compose.runtime)
    debugImplementation(libs.compose.ui.tooling)
    implementation(libs.compose.ui.tooling.preview)

    implementation(libs.compose.material)

    debugImplementation(libs.compose.ui.test.manifest)
    debugImplementation(libs.androidx.customview.customview)
    debugImplementation(libs.androidx.customview.poolingcontainer)

    testImplementation(libs.junit)
    androidTestImplementation(libs.kotlin.coroutines.test)
    androidTestImplementation(libs.androidx.junit.ext)
    androidTestImplementation(libs.androidx.test.core)
    androidTestImplementation(libs.androidx.test.rules)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.compose.ui.test)
    androidTestImplementation(libs.compose.ui.test.junit4)
}

ext["signing.keyId"] = ""
ext["signing.password"] = ""
ext["signing.key"] = ""
ext["ossUserName"] = ""
ext["ossPassword"] = ""

val javadocJar by tasks.registering(Jar::class) {
    archiveClassifier.set("javadoc")
}

val sourcesJar by tasks.registering(Jar::class) {
    archiveClassifier.set("sources")
    from(android.sourceSets.getByName("main").java.srcDirs)
}

artifacts {
    archives(sourcesJar.get())
    archives(javadocJar.get())
}

afterEvaluate {
    val secretPropsFile = project.rootProject.file("local.properties")
    if (secretPropsFile.exists()) {
        secretPropsFile.reader().use {
            Properties().apply {
                load(it)
            }
        }.onEach { (name, value) ->
            ext[name.toString()] = value
        }
    } else {
        ext["ossUsername"] = System.getenv("OSS_USERNAME")
        ext["ossPassword"] = System.getenv("OSS_PASSWORD")
        ext["signing.keyId"] = System.getenv("SIGNING_KEY_ID")
        ext["signing.password"] = System.getenv("SIGNING_PASSWORD")
        ext["signing.key"] = System.getenv("SIGNING_KEY")
    }

    publishing {
        repositories {
            maven {
                name = "sonatype"
                setUrl("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
                credentials {
                    username = extra.get("ossUsername").toString()
                    password = extra.get("ossPassword").toString()
                }
            }
        }
        publications {
            create<MavenPublication>("release") {
                groupId = "io.github.lhoyong"
                artifactId = "swiper"
                version = "1.0.2"

                if (project.plugins.hasPlugin("com.android.library")) {
                    from(components.getByName("release"))
                } else {
                    from(components.getByName("java"))
                }

                artifact(javadocJar.get())
                artifact(sourcesJar.get())

                pom {
                    name.set(artifactId)
                    description.set("Android Jetpack Compose swipe library")
                    url.set("https://github.com/lhoyong/swiper")

                    licenses {
                        license {
                            name.set("The Apache License, Version 2.0")
                            url.set("https://github.com/lhoyong/swiper/blob/main/LICENSE")
                        }
                    }

                    developers {
                        developer {
                            id.set("lhoyong")
                            name.set("hoyong")
                            email.set("lee199402@gmail.com")
                        }
                    }

                    scm {
                        connection.set("scm:git:github.com/lhoyong/swiper.git")
                        url.set("https://github.com/lhoyong/swiper")
                    }
                }
            }
        }
    }

    signing {
        useInMemoryPgpKeys(
            extra.get("signing.keyId").toString(),
            extra.get("signing.key").toString(),
            extra.get("signing.password").toString(),
        )
        sign(publishing.publications)
    }
}
