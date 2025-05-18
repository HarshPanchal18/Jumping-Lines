package com.harsh.jumpinglines.utils

import com.intellij.ide.plugins.PluginManagerCore
import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.editor.*
import com.intellij.openapi.extensions.PluginId

val AnActionEvent.editor: Editor
    get() = this.getData(CommonDataKeys.EDITOR)
        ?: error("Editor is required but not available in this context.")

fun properties(): PropertiesComponent = PropertiesComponent.getInstance()

val NumberOfForwardLines: Int
    get() = properties().getInt(Const.FORWARD_LINES, 4)

val NumberOfBackwardLines: Int
    get() = properties().getInt(Const.BACKWARD_LINES, 2)

val jumpScore: Long
    get() = properties().getLong(Const.JUMP_SCORE, 0)

val lastNotificationShown: Long
    get() = properties().getLong(Const.LAST_RATING_PROMPT, 0L)

val hasPluginReviewed: Boolean
    get() = properties().getBoolean(Const.HAS_PLUGIN_REVIEWED, false)

fun Long.inHumanReadableForm(): String {
    return when {
        this >= 1_000_000_000 -> String.format("%.2fB", this / 1_000_000_000.0) // Billions
        this >= 1_000_000 -> String.format("%.2fM", this / 1_000_000.0) // Millions
        this >= 1_00_000 -> String.format("%.2fL", this / 1_00_000.0) // Lacs
        this >= 1_000 -> String.format("%.2fK", this / 1_000.0) // Thousands
        else -> this.toString() // Under 1000
    }
}

fun increaseJumpScoreBy(score: Int) {
    properties().setValue(Const.JUMP_SCORE, (jumpScore + score).toString())
}

fun getPluginVersion(): String? {
    val pluginId = PluginId.getId("com.example.JumpingLines")
    val pluginDescriptor = PluginManagerCore.getPlugin(pluginId)

    return pluginDescriptor?.version
}

fun calculateForwardOffset(document: Document, foldingModel: FoldingModel, currentOffset: Int, linesToJump: Int): Int {

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

fun calculateBackwardOffset(document: Document, foldingModel: FoldingModel, currentOffset: Int, linesToJump: Int): Int {

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