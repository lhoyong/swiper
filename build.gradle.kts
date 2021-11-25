// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.0.3")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.31")
    }
}

subprojects {
    val ktlint: Configuration by configurations.creating
    dependencies {
        ktlint("com.pinterest:ktlint:0.42.1")
    }

    tasks.register<JavaExec>("ktlint") {
        group = "verification"
        description = "Check Kotlin code style."
        classpath = ktlint
        main = "com.pinterest.ktlint.Main"
        args("src/**/*.kt")
    }

    tasks.register<JavaExec>("ktlintFormat") {
        group = "formatting"
        description = "Fix Kotlin code style deviations."
        classpath = ktlint
        main = "com.pinterest.ktlint.Main"
        args("-F", "src/**/*.kt")
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

}

pluginManager.withPlugin("kotlin-kapt") {
    configure<org.jetbrains.kotlin.gradle.plugin.KaptExtension> { useBuildCache = true }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
