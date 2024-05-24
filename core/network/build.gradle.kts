plugins {
    id(libs.plugins.android.library.get().pluginId)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.marioloncar.core.network"

    compileSdk = 34
}

dependencies {
    implementation(libs.kotlinx.serialization.json)
    implementation(project.dependencies.platform(libs.ktor.bom))
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.timber)
}
