plugins {
    id("random.android.library")
    id("random.android.library.compose")
}

android {
    namespace = "com.haman.core.designsystem"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:model"))
}