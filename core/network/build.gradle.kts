plugins {
    id("random.android.library")
    id("random.android.hilt")
}

android {
    namespace = "com.haman.core.network"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:model"))
    implementation(project(":core:testing"))

    implementation(libs.bundles.retrofit)
    implementation(libs.bundles.paging)

    testImplementation(libs.bundles.test)
    testImplementation(libs.okhttp.mock.server)
    testImplementation(libs.hamcrest)
}