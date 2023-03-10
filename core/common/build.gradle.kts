plugins {
    id("random.android.library")
    id("random.android.hilt")
}

android {
    namespace = "com.haman.core.common"
}

dependencies {
    implementation(libs.bundles.kotlin.coroutine)
}