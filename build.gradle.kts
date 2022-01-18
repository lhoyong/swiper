// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(Dep.Plugins.androidGradlePlugin)
        classpath(Dep.Plugins.kotlin)
        classpath(Dep.Plugins.spotless)
    }
}

subprojects {
    repositories {
        google()
        mavenCentral()
    }
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            jvmTarget = "11"
        }
    }

    tasks.withType<org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompile> {
        kotlinOptions {
            freeCompilerArgs =
                listOf(
                    "-Xuse-experimental=kotlinx.coroutines.ExperimentalCoroutinesApi",
                    "-Xuse-experimental=kotlinx.coroutines.FlowPreview",
                    "-Xuse-experimental=kotlinx.serialization.ExperimentalSerializationApi",
                    "-XXLanguage:+NonParenthesizedAnnotationsOnFunctionalTypes",
                    "-P",
                    "plugin:androidx.compose.compiler.plugins.kotlin:suppressKotlinVersionCompatibilityCheck=true",

                    )
        }
    }

    afterEvaluate {
        project.apply("$rootDir/spotless.gradle")
        // used ./gradlew spotlessCheck
        // used ./gradlew spotlessApply
    }
}

pluginManager.withPlugin("kotlin-kapt") {
    configure<org.jetbrains.kotlin.gradle.plugin.KaptExtension> { useBuildCache = true }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
