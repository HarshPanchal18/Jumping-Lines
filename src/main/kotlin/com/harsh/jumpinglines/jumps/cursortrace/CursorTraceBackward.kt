package com.harsh.jumpinglines.jumps.cursortrace

import com.harsh.jumpinglines.notification.showNotification
import com.harsh.jumpinglines.utils.Jumper
import com.harsh.jumpinglines.utils.editor
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.editor.*
import com.intellij.openapi.project.DumbAwareAction
import kotlin.math.abs

class CursorTraceBackward : DumbAwareAction() {
    override fun actionPerformed(event: AnActionEvent) {

        event.project ?: return

        try {
            val editor: Editor = event.editor
            val document: Document = editor.document
            val caretModel: CaretModel = editor.caretModel

            val currentOffset = caretModel.offset
            val targetOffset = Jumper.calculateBackwardOffset(
                document = editor.document,
                foldingModel = editor.foldingModel,
                currentOffset = currentOffset,
                linesToJump = Jumper.NumberOfBackwardLines
            )

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
            val newLogicalPosition = LogicalPosition(targetLine, column)
            caretModel.moveToLogicalPosition(newLogicalPosition)
            editor.scrollingModel.scrollTo(newLogicalPosition, ScrollType.RELATIVE)

            val score = abs(currentLine - targetLine)
            Jumper.increaseJumpScoreBy(score)

        } catch (e: AssertionError) {
            showNotification("Nope, cursor can't jump outside the editor.")
        }

    }
}