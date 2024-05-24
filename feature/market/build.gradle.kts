plugins {
    id(libs.plugins.android.library.get().pluginId)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.marioloncar.feature.market"

    compileSdk = 34
}

kotlin {
    jvmToolchain(JavaVersion.VERSION_17.majorVersion.toInt())
}

dependencies {
    implementation(project(":core:ui"))
    implementation(project(":core:presentation"))
    implementation(project(":data:tickers"))
    implementation(project.dependencies.platform(libs.kotlinx.coroutines.bom))
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.timber)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(project.dependencies.platform(libs.koin.bom))
    implementation(libs.koin.core)
    implementation(libs.koin.android)
    implementation(libs.koin.compose)
}
