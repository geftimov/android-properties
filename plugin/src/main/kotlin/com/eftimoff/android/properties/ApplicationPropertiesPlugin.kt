package com.eftimoff.android.properties

import com.eftimoff.android.properties.source.sources
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType
import java.io.File

class ApplicationPropertiesPlugin : Plugin<Project> {

    companion object {
        private const val PROPERTIES_GENERATED_SOURCE_FOLDER = "generated/kotlin/config"
        private const val PROFILE_PROPERTY_NAME = "profile"
    }

    override fun apply(project: Project) {
        project.extensions.add(ApplicationPropertiesExtension.NAME, ApplicationPropertiesExtension())
        project.afterEvaluate(::init)
    }

    private fun init(project: Project) {
        val profile = getTargetProfile(project)
        val propertiesFile = getPropertiesFile(project)
        val outputDirectory = File(project.buildDir, PROPERTIES_GENERATED_SOURCE_FOLDER)

        val task = project.tasks.register(
            ApplicationPropertiesTask.NAME,
            ApplicationPropertiesTask::class.java
        ) { task ->
            task.generatedSourceOutput = outputDirectory
            task.profile.set(profile)
            task.propertiesFile.set(propertiesFile)
        }

        val buildTask = project.tasks.findByName("build")
        buildTask?.dependsOn(task)

//        val sources = sources(project)
//
//        val common = sources.singleOrNull { it.type == KotlinPlatformType.common }
//        common?.sourceDirectorySet?.srcDir(outputDirectory.toRelativeString(project.projectDir))
//
//        sources.forEach { source ->
//            if (common == null) {
//                source.sourceDirectorySet.srcDir(outputDirectory.toRelativeString(project.projectDir))
//            }
//
//            source.registerTaskDependency(task)
//        }
    }

    private fun getTargetProfile(project: Project): String =
        if (project.hasProperty(PROFILE_PROPERTY_NAME)) {
            project.property(PROFILE_PROPERTY_NAME) as String
        } else {
            val extension = project.extensions.getByName(ApplicationPropertiesExtension.NAME) as ApplicationPropertiesExtension
            extension.defaultProfile
        }

    private fun getPropertiesFile(project: Project): String {
        val extension = project.extensions.getByName(ApplicationPropertiesExtension.NAME) as ApplicationPropertiesExtension
        return extension.propertiesFile
    }
}