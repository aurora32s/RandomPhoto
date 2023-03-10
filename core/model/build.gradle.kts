plugins {
    id("random.android.library")
    id("random.android.hilt")
    id("kotlinx-serialization")
}

android {
    namespace = "com.haman.core.model"
}

dependencies {
    implementation(libs.kotlinx.serialization.json)
}