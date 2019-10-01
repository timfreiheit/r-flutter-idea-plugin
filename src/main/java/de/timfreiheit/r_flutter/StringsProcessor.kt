package de.timfreiheit.r_flutter

import com.intellij.openapi.project.Project
import org.json.JSONObject
import org.yaml.snakeyaml.Yaml
import java.io.File

fun getDefaultLocalizationFile(project: Project): File? {
    val yaml = Yaml()
    val map: Map<String, Any> = yaml.load(File(project.basePath, "pubspec.yaml").bufferedReader().readText())
    val intl = (map.get("r_flutter") as? Map<*, *>)
            ?.get("intl") as? String ?: return null
    return File(project.basePath, intl)
}

fun addNewTranslation(file: File, key: String, value: String?) {
    val jsonObject = if (file.exists()){
        JSONObject(file.bufferedReader().readText())
    } else {
        JSONObject()
    }
    jsonObject.put(key, value)
    file.writeText(jsonObject.toString(4))
}