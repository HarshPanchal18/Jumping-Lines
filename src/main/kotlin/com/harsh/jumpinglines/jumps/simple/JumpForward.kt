package com.harsh.jumpinglines.jumps.simple

import com.harsh.jumpinglines.notification.showNotification
import com.harsh.jumpinglines.utils.Jumper
import com.harsh.jumpinglines.utils.editor
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.DumbAwareAction

class JumpForward : DumbAwareAction() {

    override fun actionPerformed(event: AnActionEvent) {

        event.project ?: return

        try {

            val editor: Editor = event.editor
            val document = editor.document
            val currentOffset: Int = editor.caretModel.offset

            // Calculate the new caret position while skipping folded regions
            val targetOffset = Jumper.calculateForwardOffset(
                document = editor.document,
                foldingModel = editor.foldingModel,
                currentOffset = currentOffset,
                linesToJump = Jumper.NumberOfForwardLines
            )

            Jumper.moveCaretAndScroll(editor = editor, toOffset = targetOffset)

            val currentLineNumber = document.getLineNumber(targetOffset)
            val forwardLineNumber =
                (currentLineNumber + Jumper.NumberOfForwardLines).coerceIn(0, document.lineCount)
            val backwardLineNumber =
                (currentLineNumber - Jumper.NumberOfBackwardLines).coerceIn(0, document.lineCount)
            println("FB")
            println(forwardLineNumber)
            println(backwardLineNumber)
            println()

            Jumper.updateJumpLineMarkers(
                project = editor.project!!, document = editor.document, targetOffset = targetOffset
            )

            Jumper.updateJumpScore(document = editor.document, fromOffset = currentOffset, toOffset = targetOffset)

        } catch (e: AssertionError) {
            showNotification("Nope, cursor can't jump outside the editor.")
        }

    }

    override fun update(e: AnActionEvent) {
        val project = e.project
        e.presentation.isEnabled = project != null
    }

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.EDT
    }
}