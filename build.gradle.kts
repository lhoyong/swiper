// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(libs.kotlin.gradle.plugin)
        classpath(libs.spotless.gradle.plugin)
        classpath(libs.android.gradle.plugin)
    }
}

subprojects {
    repositories {
        google()
        mavenCentral()
    }
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            val args = mutableListOf("-opt-in=kotlin.RequiresOptIn",)

            /**
             * @see https://chris.banes.dev/composable-metrics/
             * ./gradlew assembleRelease -Pswiper.enableComposeCompilerReports=true
             */
            if (project.findProperty("swiper.enableComposeCompilerReports") == "true") {
                args.addAll(
                    listOf(
                        "-P",
                        "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=" +
                                project.buildDir.absolutePath + "/compose_metrics"
                    )
                )
                args.addAll(
                    listOf(
                        "-P",
                        "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=" +
                                project.buildDir.absolutePath + "/compose_metrics"
                    )
                )
            }

            freeCompilerArgs = args
        }
    }

    afterEvaluate {
        project.apply("$rootDir/spotless.gradle")
    }
}

pluginManager.withPlugin("kotlin-kapt") {
    configure<org.jetbrains.kotlin.gradle.plugin.KaptExtension> { useBuildCache = true }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
