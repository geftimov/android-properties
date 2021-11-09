package com.eftimoff.android.properties

import java.io.File
import java.util.*

val configurationFile = """
    package wtf.generated
    
    object ApplicationConfig {
        &properties
        
    }  
""".trimIndent()

class ApplicationPropertiesConfigurator(
    private val generatedSourceOutput: File
) {

    companion object {
        private const val APPLICATION_CONFIG_FILE_NAME = "ApplicationConfig.kt"
        private const val PROPERTIES = "&properties"
    }

    private val applicationConfigFile = File(
        generatedSourceOutput,
        APPLICATION_CONFIG_FILE_NAME
    )

    fun generateConfig(properties: Properties) {
        createGeneratedSourcesIfNeeded()

        val propertiesVariables = properties.toSortedKeysList().fold("") { accumulation, property ->
            val key = property.first.toString().toUpperCase()
            accumulation + "\n\tconst val $key = ${property.second}"
        }

        applicationConfigFile.writeText(getConfigContent(propertiesVariables))
    }

    private fun Properties.toSortedKeysList() =
        toSortedMap { key1, key2 ->
            (key1 as String).compareTo(key2 as String)
        }.toList()

    private fun createGeneratedSourcesIfNeeded() {
        if (!generatedSourceOutput.exists()) {
            generatedSourceOutput.mkdirs()
        }
    }

    private fun getConfigContent(propertiesVariables: String) =
        configurationFile.replace(PROPERTIES, propertiesVariables)
}