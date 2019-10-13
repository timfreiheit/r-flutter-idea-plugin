package de.timfreiheit.r_flutter.processor

import com.intellij.openapi.project.Project
import de.timfreiheit.r_flutter.actions.ui.showCreateNewStringKeyDialog
import de.timfreiheit.r_flutter.data.StringResource
import org.json.JSONObject
import java.io.File

fun createNewResource(project: Project, resource: StringResource? = null) : StringResource? {
    val intlFile = project.getRFlutterConfig()?.i18nFile ?: return null
    val newResource = showCreateNewStringKeyDialog(resource) ?: return null

    addNewTranslation(intlFile, newResource.key, newResource.value)
    Flutter(project).generate()
    return newResource
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