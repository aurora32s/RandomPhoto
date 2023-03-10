plugins {
    id("random.android.library")
    id("random.android.library.compose")
}

android {
    namespace = "com.haman.core.ui"
}

dependencies {
    implementation(libs.bundles.paging)

    implementation(project(":core:model"))
    implementation(project(":core:common"))
    implementation(project(":core:designsystem"))
}