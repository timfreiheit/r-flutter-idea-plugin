package de.timfreiheit.r_flutter.inspections

import com.intellij.codeInspection.InspectionToolProvider

class FlutterI18nInspectionProvider : InspectionToolProvider {
    override fun getInspectionClasses(): Array<Class<*>> = arrayOf(
        CreateStringInspector::class.java
    )
}