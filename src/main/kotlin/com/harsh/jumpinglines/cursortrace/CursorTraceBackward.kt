package com.harsh.jumpinglines.cursortrace

import com.harsh.jumpinglines.notification.showNotification
import com.harsh.jumpinglines.utils.NumberOfBackwardLines
import com.harsh.jumpinglines.utils.calculateBackwardOffset
import com.harsh.jumpinglines.utils.editor
import com.harsh.jumpinglines.utils.increaseJumpScoreBy
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.editor.LogicalPosition
import com.intellij.openapi.editor.ScrollType
import com.intellij.openapi.project.DumbAwareAction
import kotlin.math.abs

class CursorTraceBackward : DumbAwareAction() {
    override fun actionPerformed(event: AnActionEvent) {

        event.project ?: return

        try {
            val editor = event.editor
            val document = editor.document
            val scrollingModel = editor.scrollingModel
            val foldingModel = editor.foldingModel
            val caretModel = editor.caretModel

            val currentOffset = caretModel.offset
            val targetOffset = calculateBackwardOffset(
                document = document,
                foldingModel = foldingModel,
                currentOffset = currentOffset,
                linesToJump = NumberOfBackwardLines
            )

            val currentLine = document.getLineNumber(currentOffset)
            val targetLine = document.getLineNumber(targetOffset)

            if (currentLine == targetLine) return  // Nothing to do

            // Determine range of lines to add carets
            val fromLine = minOf(currentLine, targetLine) + 1
            val toLine = maxOf(currentLine, targetLine)

            val column = caretModel.logicalPosition.column

            // Clear previous selections and carets
            // caretModel.removeSecondaryCarets()

            for (line in fromLine..toLine) {
                val lineStartOffset = document.getLineStartOffset(line)
                val lineEndOffset = document.getLineEndOffset(line)
                val offset = minOf(lineStartOffset + column, lineEndOffset)
                caretModel.addCaret(editor.offsetToVisualPosition(offset))
            }

            // Scroll to target line
            val newPosition = LogicalPosition(targetLine, column)
            caretModel.moveToLogicalPosition(newPosition)
            scrollingModel.scrollTo(newPosition, ScrollType.RELATIVE)

            val score = abs(currentLine - targetLine)
            increaseJumpScoreBy(score)

        } catch (e: AssertionError) {
            showNotification("Nope, cursor can't jump outside the editor.")
        }

    }
}