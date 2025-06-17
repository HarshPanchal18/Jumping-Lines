package com.harsh.jumpinglines.jumps.cursortrace

import com.harsh.jumpinglines.notification.showNotification
import com.harsh.jumpinglines.utils.Jumper
import com.harsh.jumpinglines.utils.editor
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.editor.*
import com.intellij.openapi.project.DumbAwareAction
import kotlin.math.abs

class CursorTraceForward : DumbAwareAction() {
    override fun actionPerformed(event: AnActionEvent) {

        event.project ?: return

        try {
            val editor: Editor = event.editor
            val document: Document = editor.document
            val caretModel: CaretModel = editor.caretModel

            val currentOffset = caretModel.offset
            val targetOffset = Jumper.jumpForwardPreservingFolds(editor)

            val currentLine = document.getLineNumber(currentOffset)
            val targetLine = document.getLineNumber(targetOffset)

            val column = caretModel.logicalPosition.column
            Jumper.addCaretsOnJumpedLines(
                editor = editor,
                currentLine = currentLine,
                targetLine = targetLine,
                column = column
            )

            // Move caret and scroll editor to the new logical position
            val newPosition = LogicalPosition(targetLine, column)
            caretModel.moveToLogicalPosition(newPosition)
            editor.scrollingModel.scrollTo(newPosition, ScrollType.RELATIVE)

            val score = abs(currentLine - targetLine)
            Jumper.increaseJumpScoreBy(score)

        } catch (e: AssertionError) {
            showNotification("Nope, cursor can't jump outside the editor.")
        }

    }
}