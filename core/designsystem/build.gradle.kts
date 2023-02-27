plugins {
    id("daangn.android.library")
    id("daangn.android.library.compose")
}

android {
    namespace = "com.haman.core.designsystem"
}

dependencies {
    implementation(project(":core:common"))
}