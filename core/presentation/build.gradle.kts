plugins {
    id(libs.plugins.android.library.get().pluginId)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.marioloncar.core.presentation"

    compileSdk = 34
}

kotlin {
    jvmToolchain(JavaVersion.VERSION_17.majorVersion.toInt())
}

dependencies {
    implementation(project(":core:util"))
    implementation(project.dependencies.platform(libs.kotlinx.coroutines.bom))
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.timber)
    implementation(libs.androidx.core.ktx)
}
