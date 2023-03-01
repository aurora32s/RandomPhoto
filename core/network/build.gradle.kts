plugins {
    id("daangn.android.library")
    id("daangn.android.hilt")
}

android {
    namespace = "com.haman.core.network"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:model"))

    implementation(libs.bundles.retrofit)
    implementation(libs.bundles.paging)

    testImplementation(libs.bundles.test)
    testImplementation(libs.okhttp.mock.server)
}