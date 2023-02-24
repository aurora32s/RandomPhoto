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
}