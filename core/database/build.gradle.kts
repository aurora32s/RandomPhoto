plugins {
    id("random.android.library")
    id("random.android.hilt")
    id("random.android.room")
}

android {
    namespace = "com.haman.core.database"
}

dependencies {
    implementation(project(":core:model"))
    implementation(libs.room.paging)
}