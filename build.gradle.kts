// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        maven {
            setUrl("https://storage.googleapis.com/r8-releases/raw")
        }
    }
    dependencies {
        classpath(libs.android.tools.r8)
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
    alias(libs.plugins.detekt)
}

tasks.detekt {
    description = "Custom DETEKT build for all modules"
    parallel = true
    ignoreFailures = false
    buildUponDefaultConfig = true
    config.setFrom("$rootDir/staticAnalysis/detekt.yml")
    setSource("$projectDir")
    exclude(
        "**/build.gradle.kts",
        "**/test/**",
        "**/androidTest/**"
    )
}
