plugins {
    id ("daangn.android.library")
    id("daangn.android.hilt")
}

android {
    namespace = "com.haman.core.network"
}

dependencies {
    implementation(libs.bundles.retrofit)
}