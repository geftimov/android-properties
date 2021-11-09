package com.eftimoff.android.properties

import com.eftimoff.android.properties.ApplicationPropertiesExtension.Companion.DEFAULT_PROFILE_NAME
import com.eftimoff.android.properties.ApplicationPropertiesExtension.Companion.DEFAULT_PROPERTIES_FILE
import java.io.File
import java.io.FileInputStream
import java.util.Properties
import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.*

//@CacheableTask
abstract class ApplicationPropertiesTask : DefaultTask() {

  companion object {
    const val NAME = "buildApplicationProperties"
  }

  init {
    group = "application properties"
    description = "Build configuration from properties file."
  }

  @get:OutputDirectory
  var generatedSourceOutput: File? = null
  @get:Input
  abstract val profile: Property<String>
  @get:Input
  abstract val propertiesFile: Property<String>

  @TaskAction
  fun build() {
    println("Building configuration for profile: ${propertiesFile.get()}")
    val properties = getProperties(propertiesFile.get())

    properties.forEach { key, value ->
      println("Key : $key, Value : $value")
    }

    generatedSourceOutput?.run {
      print("Building configuration for profile: $profile.properties")

      deleteRecursively()
      val kotlinConfigGenerator = ApplicationPropertiesConfigurator(this)
      kotlinConfigGenerator.generateConfig(properties)
    }
  }

  private fun getProperties(propertiesFile: String): Properties {
    val localPropertiesFile = project.file(propertiesFile)
    val localProperties = Properties()
    localProperties.load(FileInputStream(localPropertiesFile))
    return localProperties
  }
}