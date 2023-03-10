plugins {
    id("random.android.feature")
    id("random.android.library.compose")
    id("kotlinx-serialization")
}

android {
    namespace = "com.haman.feature.detail"
}

dependencies {
    implementation(project(":core:designsystem"))
    implementation(libs.kotlinx.serialization.json)
}