plugins {
    id("random.android.library")
    id("random.android.hilt")
}

android {
    namespace = "com.haman.core.database"
}

dependencies {
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    kapt(libs.room.compiler)
}