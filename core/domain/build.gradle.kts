plugins {
    id ("daangn.android.library")
    id("daangn.android.hilt")
}

android {
    namespace = "com.haman.core.domain"
}

dependencies {
    implementation(project(":core:data"))
    implementation(project(":core:model"))

    implementation(libs.bundles.paging)
}