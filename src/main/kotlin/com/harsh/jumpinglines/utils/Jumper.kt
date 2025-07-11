package com.harsh.jumpinglines.utils

import com.harsh.jumpinglines.utils.DecorationManager.addDecorationForCaret
import com.harsh.jumpinglines.utils.DecorationManager.clearAllDecorations
import com.harsh.jumpinglines.utils.DecorationManager.clearAllLineFlashes
import com.harsh.jumpinglines.utils.DecorationManager.flashLine
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.editor.*
import com.intellij.openapi.editor.event.CaretEvent
import com.intellij.openapi.editor.event.CaretListener
import java.awt.Color
import kotlin.math.abs

object Jumper {

    val jumpScore: Long
        get() = properties().getLong(Const.JUMP_SCORE, 0)

    val NumberOfForwardLines: Int
        get() = properties().getInt(Const.FORWARD_LINES, 4)

    val NumberOfBackwardLines: Int
        get() = properties().getInt(Const.BACKWARD_LINES, 2)

    val IsMarkerEnabled: Boolean
        get() = properties().getBoolean(Const.IS_MARKER_ENABLED, false)

    val IsSoundEnabled: Boolean
        get() = properties().getBoolean(Const.IS_SOUND_ENABLED, false)

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

    // Performs a forward jump while preserving the original folding state.
    fun jumpForwardPreservingFolds(editor: Editor): Int {
        val foldingModel = editor.foldingModel
        val caretModel = editor.caretModel

        // Capture the folding regions that are currently collapsed
        val previouslyCollapsedRegions = foldingModel.allFoldRegions.filter { !it.isExpanded }

        // Calculate the target offset while skipping over folded regions
        val targetOffset = calculateForwardOffset(
            document = editor.document,
            foldingModel = foldingModel,
            currentOffset = caretModel.offset,
            linesToJump = NumberOfForwardLines
        )

        // Perform caret move and folding restore inside a batch operation
        ApplicationManager.getApplication().runWriteAction {
            foldingModel.runBatchFoldingOperation {

                // Move the caret to the target position
                caretModel.moveToOffset(targetOffset)

                // Restore previously collapsed regions (re-collapse)
                previouslyCollapsedRegions.forEach { region ->
                    if (region.isValid)
                        region.isExpanded = false
                }
            }
        }

        return targetOffset
    }

    // Performs a backward jump while preserving the original folding state.
    fun jumpBackwardPreservingFolds(editor: Editor): Int {
        val foldingModel = editor.foldingModel
        val caretModel = editor.caretModel

        // Capture the folding regions that are currently collapsed
        val previouslyCollapsedRegions = foldingModel.allFoldRegions.filter { !it.isExpanded }

        // Calculate the target offset while skipping over folded regions
        val targetOffset = calculateBackwardOffset(
            document = editor.document,
            foldingModel = foldingModel,
            currentOffset = caretModel.offset,
            linesToJump = NumberOfBackwardLines
        )

        ApplicationManager.getApplication().runWriteAction {
            foldingModel.runBatchFoldingOperation {

                // Move the caret to the target position
                caretModel.moveToOffset(targetOffset)

                // Restore previously collapsed regions (re-collapse)
                previouslyCollapsedRegions.forEach { region ->
                    if (region.isValid)
                        region.isExpanded = false
                }
            }
        }

        return targetOffset
    }

    fun addCaretsOnJumpedLines(editor: Editor, currentLine: Int, targetLine: Int, column: Int) {

        val caretModel: CaretModel = editor.caretModel
        val document: Document = editor.document
        val selectionModel: SelectionModel = editor.selectionModel

        if (currentLine == targetLine) return  // Nothing to do

        // Listen events of caret(cursor).
        if (properties().getBoolean(Const.IS_MARKER_ENABLED)) {
            caretModel.addCaretListener(object : CaretListener {
                override fun caretAdded(event: CaretEvent) {}
                override fun caretPositionChanged(event: CaretEvent) {}
                override fun caretRemoved(event: CaretEvent) {
                    if (caretModel.caretCount == 1) {
                        clearAllDecorations(editor)
                    }
                }
            })
        }

        // Remove selection blocks before jumping (if any).
        if (selectionModel.hasSelection()) {
            selectionModel.removeSelection(/* allCarets = */ true)
        }

        // Determine direction to go on.
        val lines = if (targetLine > currentLine) {
            (currentLine + 1)..targetLine  // Forward direction
        } else {
            (currentLine - 1) downTo targetLine // Backward direction
        }

        // For each line in the jump range, add a caret at the correct column
        for (line in lines) {
            val lineStartOffset = document.getLineStartOffset(line)
            val lineEndOffset = document.getLineEndOffset(line)

            // Ensure the caret column does not go past the end of the line
            val offset = minOf(lineStartOffset + column, lineEndOffset)

            val visualPosition = editor.offsetToVisualPosition(offset)

            // Check if any caret already exists at this visual line
            val caretExists = caretModel.allCarets.any { it.visualPosition.line == visualPosition.line }

            // If there is no caret on current line, only then put cursor.
            if (!caretExists) {
                caretModel.addCaret(visualPosition)

                // Add markers to lines the cursor is covering.
                if (properties().getBoolean(Const.IS_MARKER_ENABLED))
                    addDecorationForCaret(editor = editor, offset = offset, guideColor = Color(255, 100, 33))

            } else {
                // Otherwise remove cursor on the current line. Helps in changing the direction.
                caretModel.removeCaret(caretModel.currentCaret)

                // Remove markers from lines the cursor is no longer.
                if (properties().getBoolean(Const.IS_MARKER_ENABLED))
                    DecorationManager.removeDecorationAtOffset(editor, offset)
            }

            // Add a new caret at the calculated visual position
            caretModel.addCaret(editor.offsetToVisualPosition(offset))

        }

    }

    fun moveCaretAndScroll(editor: Editor, toOffset: Int) {
        if (properties().getBoolean(Const.IS_MARKER_ENABLED)) {
            // Clear markers of previous jump (if any).
            clearAllDecorations(editor)
            clearAllLineFlashes(editor)
        }

        val caretModel = editor.caretModel
        val document = editor.document
        val selectionModel = editor.selectionModel

        // Remove already selected block(s) if any.
        if (selectionModel.hasSelection()) {
            selectionModel.removeSelection(true)
        }

        // Remove secondary cursors if any.
        if (caretModel.caretCount > 1) {
            caretModel.removeSecondaryCarets()
        }

        // Move caret to the target offset
        caretModel.moveToOffset(toOffset)

        // snap to the visual start of that line
        // – for an ordinary line this is column 0
        // – for a collapsed fold this is the fold header position
        val snappedOffset = caretModel.visualLineStart
        if (snappedOffset != caretModel.offset) {
            caretModel.moveToOffset(snappedOffset)
        }

        // Move caret and scroll editor to the new logical position
        editor.scrollingModel.scrollToCaret(ScrollType.RELATIVE)

        if (properties().getBoolean(Const.IS_MARKER_ENABLED)) {
            val backwardGuideLineNumber =
                (document.getLineNumber(toOffset) - NumberOfBackwardLines)
                    .coerceIn(minimumValue = 0, document.lineCount - 1)
            val backwardOffset = document.getLineStartOffset(backwardGuideLineNumber)
            flashLine(editor, backwardOffset, Color(24, 163, 232))

            val forwardGuideLineNumber =
                (document.getLineNumber(toOffset) + NumberOfForwardLines)
                    .coerceIn(minimumValue = 0, document.lineCount - 1)
            val forwardOffset = document.getLineStartOffset(forwardGuideLineNumber)
            flashLine(editor, forwardOffset, Color(254, 242, 23))
        }

    }

    fun moveCaretAndScrollWithSelection(editor: Editor, currentOffset: Int) {
        val caretModel = editor.caretModel
        val selectionModel = editor.selectionModel

        // If there is a selection, start from the beginning of the selection; otherwise, start from the current caret position
        val startOffset: Int =
            if (selectionModel.hasSelection()) selectionModel.leadSelectionOffset else currentOffset

        // Move caret to the target offset and extend the selection
        val visualLineStart = caretModel.visualLineStart
        caretModel.moveToOffset(visualLineStart)
        selectionModel.setSelection(/* startOffset = */ startOffset, /* endOffset = */ visualLineStart)

        // scroll editor to the new caret position
        editor.scrollingModel.scrollToCaret(ScrollType.RELATIVE)
    }

    fun updateJumpScore(document: Document, fromOffset: Int, toOffset: Int) {
        // Get line numbers
        val fromLine = document.getLineNumber(fromOffset)
        val toLine = document.getLineNumber(toOffset)

        // Increase score only if cursor is jumped.
        increaseJumpScoreBy(abs(fromLine - toLine))
    }

    fun increaseJumpScoreBy(score: Int) {
        properties().setValue(Const.JUMP_SCORE, (jumpScore + score).toString())
    }

}