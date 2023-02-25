plugins {
    id ("daangn.android.library")
    id ("daangn.android.library.compose")
}

android {
    namespace = "com.haman.core.ui"
}

dependencies {
    implementation(project(":core:designsystem"))
}