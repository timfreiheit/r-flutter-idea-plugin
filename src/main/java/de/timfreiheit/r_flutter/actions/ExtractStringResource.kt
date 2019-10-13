package de.timfreiheit.r_flutter.actions


import com.intellij.codeInsight.intention.HighPriorityAction
import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction
import com.intellij.openapi.command.WriteCommandAction.runWriteCommandAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.jetbrains.lang.dart.psi.DartStringLiteralExpression
import de.timfreiheit.r_flutter.data.StringResource
import de.timfreiheit.r_flutter.processor.createNewResource

class ExtractStringResource : PsiElementBaseIntentionAction(), HighPriorityAction {

    override fun getText(): String = "Extract string resource"

    override fun getFamilyName(): String = text

    override fun isAvailable(project: Project, editor: Editor?, element: PsiElement): Boolean {
        return editor?.let {
            val stringLiteral = PsiTreeUtil.getParentOfType(element, DartStringLiteralExpression::class.java)
            stringLiteral != null && stringLiteral.text.length > 2
        } ?: false
    }

    override fun invoke(project: Project, editor: Editor, element: PsiElement) {
        val psiFile = PsiDocumentManager.getInstance(project).getPsiFile(editor.document) ?: return
        val dartExpression = PsiTreeUtil.getParentOfType(element, DartStringLiteralExpression::class.java)
                ?: return

        val newResource = createNewResource(project, StringResource("", dartExpression.text.substring(1, dartExpression.text.length - 2)))
                ?: return

        runWriteCommandAction(project, text, "r_flutter", Runnable {
            val fileText = dartExpression.text
            val newText = psiFile.text.replaceRange(
                    dartExpression.textOffset,
                    dartExpression.textOffset + fileText.length,
                    "I18n.of(context).${newResource.key.replace(".", "_")}"
            )
            editor.document.setText(newText)
        }, psiFile)
    }
}
