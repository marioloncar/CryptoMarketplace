plugins {
    id(libs.plugins.android.library.get().pluginId)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.marioloncar.core.util"

    compileSdk = 34
}

dependencies {
    implementation(project.dependencies.platform(libs.kotlinx.coroutines.bom))
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.serialization.json)
    implementation(project.dependencies.platform(libs.ktor.bom))
    implementation(libs.ktor.client.core)
    implementation(libs.timber)
}
