plugins {
    id("random.android.library")
    id("random.android.hilt")
    id("random.android.room")
}

android {
    namespace = "com.haman.core.data"
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

dependencies {
    implementation(project(":core:network"))
    implementation(project(":core:datastore"))
    implementation(project(":core:database"))
    implementation(project(":core:common"))
    implementation(project(":core:model"))
    implementation(project(":core:testing"))

    implementation(libs.bundles.kotlin.coroutine)
    implementation(libs.bundles.paging)

    androidTestImplementation(libs.bundles.test)
    androidTestImplementation(libs.okhttp.mock.server)
    androidTestImplementation(libs.bundles.retrofit)
    androidTestImplementation(libs.bundles.instrument.test)
}