plugins {
    id("daangn.android.library")
    id("daangn.android.hilt")
}

android {
    namespace = "com.haman.core.datastore"
}

dependencies {
    implementation(project(":core:common"))
    implementation(libs.bundles.kotlin.coroutine)
}