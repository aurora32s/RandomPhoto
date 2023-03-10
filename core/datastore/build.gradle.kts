plugins {
    id("random.android.library")
    id("random.android.hilt")
}

android {
    namespace = "com.haman.core.datastore"
}

dependencies {
    implementation(project(":core:common"))
    implementation(libs.bundles.kotlin.coroutine)
}