plugins {
    id("daangn.android.library")
    id("daangn.android.hilt")
    id("kotlinx-serialization")
}

android {
    namespace = "com.haman.core.model"
}

dependencies {
    implementation(libs.kotlinx.serialization.json)
}