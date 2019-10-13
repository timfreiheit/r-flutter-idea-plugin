package de.timfreiheit.r_flutter.processor

import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.openapi.vfs.VirtualFile
import de.timfreiheit.r_flutter.data.RFlutterConfig
import org.yaml.snakeyaml.Yaml
import java.io.File

fun Project.getRFlutterConfig(): RFlutterConfig? {
    val pubspecFile = File(basePath, "pubspec.yaml")
    if (!pubspecFile.exists()) {
        return null
    }
    val yaml = Yaml()
    val map: Map<String, Any> = yaml.load(pubspecFile.bufferedReader().readText())

    (map.get("r_flutter") as? Map<*, *>)?.let { config ->
        return RFlutterConfig(
            i18nFile = (config.get("intl") as? String)?.let { filePath ->
                File(basePath, filePath)
            }
        )
    }
    return RFlutterConfig()
}

fun Project.findAssetsFile(): VirtualFile? {
    ProjectRootManager.getInstance(this).contentRoots.forEach {
        val generatedFolder = it.findFileByRelativePath(".dart_tool/build/generated/")
                ?: return@forEach
        val assetFile = generatedFolder.findChildRecursive("assets.dart")
        if (assetFile != null) {
            return assetFile
        }
    }
    return null
}

fun VirtualFile.findChildRecursive(name: String): VirtualFile? {
    var file = findChild(name)
    if (file != null) {
        return file
    }
    children.forEach {
        file = it.findChildRecursive(name)
        if (file != null) {
            return file
        }
    }
    return null
}