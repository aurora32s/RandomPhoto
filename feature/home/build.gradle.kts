plugins {
    id("daangn.android.feature")
    id("daangn.android.library.compose")
}

android {
    namespace = "com.haman.feature.home"
}

dependencies {
    implementation(project(":core:designsystem"))

    implementation(libs.bundles.paging)
}