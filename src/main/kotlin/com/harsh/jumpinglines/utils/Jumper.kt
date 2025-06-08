package com.harsh.jumpinglines.utils

import com.harsh.jumpinglines.jumps.gutterpreview.JumpLineStateService
import com.intellij.codeInsight.daemon.DaemonCodeAnalyzer
import com.intellij.openapi.components.service
import com.intellij.openapi.editor.*
import com.intellij.openapi.project.Project

object Jumper {

    val jumpScore: Long
        get() = properties().getLong(Const.JUMP_SCORE, 0)

    val NumberOfForwardLines: Int
        get() = properties().getInt(Const.FORWARD_LINES, 4)

    val NumberOfBackwardLines: Int
        get() = properties().getInt(Const.BACKWARD_LINES, 2)

    fun calculateForwardOffset(
        document: Document,
        foldingModel: FoldingModel,
        currentOffset: Int,
        linesToJump: Int
    ): Int {

        var linesJumped = 0
        var targetOffset = currentOffset

        // Run folding-aware navigation as a batch operation for performance
        foldingModel.runBatchFoldingOperation {
            while (linesJumped < linesToJump) {

                // Get the line number for the current offset and move one line down
                val currentLineNumber = document.getLineNumber(targetOffset)
                val newLineNumber = currentLineNumber + 1

                // Stop if we’ve reached the bottom of the document
                if (newLineNumber >= document.lineCount)
                    break

                // Get the offset at the start of the new line
                val newOffset = document.getLineStartOffset(newLineNumber)

                // Check if this offset is inside a collapsed fold region
                val foldRegion = foldingModel.getCollapsedRegionAtOffset(newOffset)

                if (foldRegion != null) {
                    // If folded, skip the region and move to the end of the fold
                    targetOffset = foldRegion.endOffset

                    // Stop if we’ve gone after the last line
                    if (document.getLineNumber(targetOffset) >= document.lineCount - 1)
                        break

                } else {
                    // Otherwise, update offset and count the line as jumped
                    targetOffset = newOffset
                    linesJumped++
                }
            }
        }

        // Return the final offset after jumping backward
        return targetOffset
    }

    fun calculateBackwardOffset(
        document: Document,
        foldingModel: FoldingModel,
        currentOffset: Int,
        linesToJump: Int
    ): Int {

        var linesJumped = 0
        var targetOffset = currentOffset

        // Run folding-aware navigation as a batch operation for performance
        foldingModel.runBatchFoldingOperation {
            while (linesJumped < linesToJump) {
                // Get the line number for the current offset and move one line up
                val currentLineNumber = document.getLineNumber(targetOffset)
                val newLineNumber = currentLineNumber - 1

                // Stop if we’ve reached the top of the document
                if (newLineNumber < 0)
                    break

                // Get the offset at the end of the new line
                val newOffset = document.getLineEndOffset(newLineNumber)

                // Check if this offset is inside a collapsed fold region
                val foldRegion = foldingModel.getCollapsedRegionAtOffset(newOffset)

                if (foldRegion != null) {
                    // If folded, skip the region and move to the start of the fold
                    targetOffset = foldRegion.startOffset

                    // Stop if we’ve gone past the first line
                    if (document.getLineNumber(targetOffset) <= 0)
                        break

                } else {
                    // Otherwise, update offset and count the line as jumped
                    targetOffset = newOffset
                    linesJumped++
                }
            }
        }

        // Return the final offset after jumping backward
        return targetOffset
    }

    fun addCaretsOnJumpedLines(editor: Editor, currentLine: Int, targetLine: Int, column: Int) {

        if (currentLine == targetLine) return  // Nothing to do

        // Remove selection blocks before jumping (if any).
        if (editor.selectionModel.hasSelection()) {
            editor.selectionModel.removeSelection(/* allCarets = */ true)
        }

        // Determine direction to go on.
        val lines = if (targetLine > currentLine) {
            (currentLine + 1)..targetLine  // Forward direction
        } else {
            (currentLine - 1) downTo targetLine // Backward direction
        }

        // For each line in the jump range, add a caret at the correct column
        for (line in lines) {
            val lineStartOffset = editor.document.getLineStartOffset(line)
            val lineEndOffset = editor.document.getLineEndOffset(line)

            // Ensure the caret column does not go past the end of the line
            val offset = minOf(lineStartOffset + column, lineEndOffset)

            // Add a new caret at the calculated visual position
            editor.caretModel.addCaret(editor.offsetToVisualPosition(offset))
        }
    }

    fun moveCaretAndScroll(editor: Editor, toOffset: Int) {
        val caretModel = editor.caretModel
        val selectionModel = editor.selectionModel

        // Remove already selected block(s) if any.
        if (selectionModel.hasSelection()) {
            selectionModel.removeSelection(true)
        }

        // Remove secondary cursors if any.
        if (caretModel.caretCount > 1) {
            caretModel.removeSecondaryCarets()
        }

        val targetLine = editor.document.getLineNumber(toOffset)
        val column = caretModel.logicalPosition.column

        // Move caret to the target offset
        caretModel.moveToOffset(toOffset)

        // Move caret and scroll editor to the new logical position
        val newLogicalPosition = LogicalPosition(targetLine, column)
        caretModel.moveToLogicalPosition(newLogicalPosition)
        editor.scrollingModel.scrollTo(newLogicalPosition, ScrollType.RELATIVE)

    }

    fun moveCaretAndScrollWithSelection(editor: Editor, currentOffset: Int, targetOffset: Int) {
        val caretModel = editor.caretModel
        val selectionModel = editor.selectionModel

        // If there is a selection, start from the beginning of the selection; otherwise, start from the current caret position
        val startOffset: Int =
            if (selectionModel.hasSelection()) selectionModel.leadSelectionOffset else currentOffset

        // Move caret to the target offset and extend the selection
        caretModel.moveToOffset(targetOffset)
        val visualLineStart = caretModel.visualLineStart
        selectionModel.setSelection(/* startOffset = */ startOffset, /* endOffset = */ visualLineStart)

        val newLineNumber = editor.document.getLineNumber(targetOffset)
        val newLogicalPosition = LogicalPosition(/* line = */ newLineNumber, /* column = */ 0)

        // Move caret and scroll editor to the new logical position
        caretModel.moveToLogicalPosition(newLogicalPosition)
        editor.scrollingModel.scrollTo(newLogicalPosition, ScrollType.RELATIVE)
    }

    fun updateJumpScore(document: Document, fromOffset: Int, toOffset: Int) {
        // Get line numbers
        val fromLine = document.getLineNumber(fromOffset)
        val toLine = document.getLineNumber(toOffset)

        if (fromLine != toLine) {
            increaseJumpScoreBy(fromLine - toLine)
        }
    }

    fun increaseJumpScoreBy(score: Int) {
        properties().setValue(Const.JUMP_SCORE, (jumpScore + score).toString())
    }

    fun updateJumpLineMarkers(project: Project, document: Document, targetOffset: Int) {

        val currentLineNumber = document.getLineNumber(targetOffset)
        val forwardLineNumber = (currentLineNumber + NumberOfForwardLines).coerceIn(0, document.lineCount)
        val backwardLineNumber = (currentLineNumber - NumberOfBackwardLines).coerceIn(0, document.lineCount)

        println("Next Backward: $backwardLineNumber")
        println("Next Forward: $forwardLineNumber")
        println()

        project.service<JumpLineStateService>().setBoundary(forwardLineNumber, backwardLineNumber)

        // Trigger a gutter refresh
        DaemonCodeAnalyzer.getInstance(project).restart()
    }

}