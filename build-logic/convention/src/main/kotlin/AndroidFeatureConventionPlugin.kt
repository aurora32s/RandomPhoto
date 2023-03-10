import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * Feature Module Convention Plugin
 */
class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                apply("random.android.library")
                apply("random.android.hilt")
            }

            // Feature Module 에서 공통적으로 사용되는 Module 추가
            dependencies {
                "implementation"(project(":core:domain"))
                "implementation"(project(":core:model"))
                "implementation"(project(":core:ui"))
                "implementation"(project(":core:common"))
            }
        }
    }
}