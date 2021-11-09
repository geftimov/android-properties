package com.eftimoff.android.properties

class ApplicationPropertiesExtension(
  var defaultProfile: String = DEFAULT_PROFILE_NAME,
  var propertiesFile: String = DEFAULT_PROPERTIES_FILE
) {

  companion object {
    const val NAME = "applicationProperties"
    const val DEFAULT_PROFILE_NAME = ""
    const val DEFAULT_PROPERTIES_FILE = "application.properties"
  }
}