plugins {
    id ("daangn.android.feature")
    id ("daangn.android.library.compose")
}

android {
    namespace = "com.haman.feature.detail"
}

dependencies {
    implementation(project(":core:designsystem"))
}