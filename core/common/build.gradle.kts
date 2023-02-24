plugins {
    id ("daangn.android.library")
    id("daangn.android.hilt")
}

android {
    namespace = "com.haman.core.common"
}

dependencies {
    implementation(libs.bundles.kotlin.coroutine)
}