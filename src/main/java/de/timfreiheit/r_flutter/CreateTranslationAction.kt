package de.timfreiheit.r_flutter

import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.codeInsight.intention.IntentionActionProvider
import com.intellij.codeInsight.intention.IntentionActionWithOptions
import com.intellij.openapi.editor.Caret
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.IncorrectOperationException

import org.jetbrains.annotations.Nls

import java.util.ArrayList
import javax.swing.JOptionPane

import javax.swing.JPanel

class CreateTranslationAction : JPanel(), IntentionActionProvider {
    override fun getIntentionAction(): IntentionActionWithOptions? {
        return Action()
    }

    internal class Action : IntentionActionWithOptions {

        override fun getOptions(): List<IntentionAction> {
            return listOf()
        }

        override fun getText(): String {
            return "New Translation (r_flutter)"
        }

        override fun getFamilyName(): String {
            return text
        }

        override fun isAvailable(project: Project, editor: Editor, file: PsiFile): Boolean {
            if (editor.caretModel.allCarets.size <= 0) {
                return false
            }
            val caret = editor.caretModel.allCarets[0]
            val psiElement = file.findElementAt(caret.offset)
            // TODO check if the psiElement is StringBinding where the action should be enabled
            return psiElement != null && psiElement.isValid && getDefaultLocalizationFile(project) != null
        }

        @Throws(IncorrectOperationException::class)
        override fun invoke(project: Project, editor: Editor, file: PsiFile) {
            val caret = editor.caretModel.allCarets[0]
            val psiElement = file.findElementAt(caret.offset)
            val newKey = psiElement?.text ?: return
            val newValue = JOptionPane.showInputDialog(
                    null,
                    "Enter default value for key \"$newKey\":",
                    text,
                    JOptionPane.PLAIN_MESSAGE)
            try {
                val intlFile = getDefaultLocalizationFile(project) ?: return
                print(intlFile.absolutePath)
                addNewTranslation(intlFile, newKey, newValue)
            } catch (e: Exception) {
                JOptionPane.showMessageDialog(null, e.message)
            }
        }

        override fun startInWriteAction(): Boolean {
            return false
        }
    }
}
