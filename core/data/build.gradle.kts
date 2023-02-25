plugins {
    id ("daangn.android.library")
    id ("daangn.android.hilt")
}

android {
    namespace = "com.haman.core.data"
}

dependencies {
    implementation(project(":core:network"))
    implementation(project(":core:datastore"))
    implementation(project(":core:common"))
    implementation(project(":core:model"))

    implementation(libs.bundles.kotlin.coroutine)
    implementation(libs.bundles.paging)
}