plugins {
    id("random.android.library")
    id("random.android.hilt")
}

android {
    namespace = "com.haman.core.domain"
}

dependencies {
    implementation(project(":core:data"))
    implementation(project(":core:model"))

    implementation(libs.bundles.paging)
}