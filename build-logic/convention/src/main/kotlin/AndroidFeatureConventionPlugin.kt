import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidFeatureConventionPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                apply("daangn.android.library")
                apply("daangn.android.hilt")
            }

            dependencies {
                "implementation"(project(":core:domain"))
                "implementation"(project(":core:model"))
                "implementation"(project(":core:ui"))
                "implementation"(project(":core:common"))
            }
        }
    }
}