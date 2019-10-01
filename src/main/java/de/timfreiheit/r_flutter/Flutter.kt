package de.timfreiheit.r_flutter

import com.intellij.openapi.project.Project
import java.io.File
import java.util.*

class Flutter(
        private val project: Project
) {

    private fun findFlutter(): String {

        fun readPropertiesFile(file: File): Properties? {
            if (!file.exists()) {
                return null
            }
            return try {
                Properties().apply {
                    load(file.bufferedReader())
                }
            } catch (e: Exception) {
                null
            }
        }

        fun readFlutterFromAndroidProperties(): String? {
            return readPropertiesFile(File(project.basePath, "android/local.properties"))
                    ?.getProperty("flutter.sdk")
        }

        fun readFlutterFromIOSXCConfig(): String? {
            return readPropertiesFile(File(project.basePath, "ios/Flutter/Generated.xcconfig"))
                    ?.getProperty("FLUTTER_ROOT")
        }

        val path = readFlutterFromAndroidProperties()
                ?: readFlutterFromIOSXCConfig()
        if (path != null) {
            return "$path/bin/flutter"
        }
        // fallback to global flutter version
        return "flutter"
    }

    fun generate() {
        val projectPath = project.basePath ?: return
        ProcessBuilder(findFlutter(), "generate")
                .directory(File(projectPath))
                .redirectOutput(ProcessBuilder.Redirect.INHERIT)
                .redirectError(ProcessBuilder.Redirect.INHERIT)
                .start()
    }
}