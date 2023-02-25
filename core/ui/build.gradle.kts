plugins {
    id("daangn.android.library")
    id("daangn.android.library.compose")
}

android {
    namespace = "com.haman.core.ui"
}

dependencies {
    implementation(libs.bundles.paging)

    implementation(project(":core:model"))
    implementation(project(":core:designsystem"))
}