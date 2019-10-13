package de.timfreiheit.r_flutter.inspections.quickfix

import com.intellij.codeInspection.LocalQuickFixOnPsiElement
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import de.timfreiheit.r_flutter.data.StringResource
import de.timfreiheit.r_flutter.processor.createNewResource

class CreateStringResourceQuickFix(element: PsiElement, private val fieldName: String) :
    LocalQuickFixOnPsiElement(element) {

    override fun getText(): String = "Create string value resource '$fieldName'"

    override fun getFamilyName(): String = text

    override fun invoke(project: Project, psiFile: PsiFile, element: PsiElement, element2: PsiElement) {
        WriteCommandAction.runWriteCommandAction(project, text, "r_flutter",
            Runnable {
                createNewResource(project, StringResource(fieldName, ""))
                        ?: return@Runnable
            },
            psiFile
        )
    }
}