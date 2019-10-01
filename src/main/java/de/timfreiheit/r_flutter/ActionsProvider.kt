package de.timfreiheit.r_flutter

import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.FileEditorManagerEvent
import com.intellij.openapi.fileEditor.FileEditorManagerListener
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.project.ProjectManagerListener
import javax.swing.JComponent

object ActionsProvider {

    internal fun register() {
        ProjectManager.getInstance().addProjectManagerListener(object : ProjectManagerListener {
            override fun projectOpened(project: Project) {
                val actions = listOf<JComponent>(CreateTranslationAction())

                FileEditorManager.getInstance(project).addFileEditorManagerListener(object : FileEditorManagerListener {
                    override fun selectionChanged(event: FileEditorManagerEvent) {
                        if (event.oldEditor != null) {
                            for (action in actions) {
                                event.manager.removeTopComponent(event.oldEditor!!, action)
                            }
                        }

                        if (event.newEditor != null && event.newFile != null && "dart" == event.newFile!!.extension) {
                            for (action in actions) {
                                event.manager.addTopComponent(event.newEditor!!, action)
                            }
                        }
                    }
                })
            }
        })
    }

}
