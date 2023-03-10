plugins {
    id("random.android.feature")
    id("random.android.library.compose")
}

android {
    namespace = "com.haman.feature.home"
}

dependencies {
    implementation(project(":core:designsystem"))

    implementation(libs.bundles.paging)
}