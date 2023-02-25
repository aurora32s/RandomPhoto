plugins {
    id("daangn.android.feature")
    id("daangn.android.library.compose")
}

android {
    namespace = "com.haman.feature.home"
}

dependencies {
    implementation(libs.bundles.paging)
}