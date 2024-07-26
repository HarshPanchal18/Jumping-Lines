package com.harsh.jumpinglines.utils

import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.FoldingModel

val AnActionEvent.editor: Editor
    get() = this.getRequiredData(CommonDataKeys.EDITOR)

fun propertiesComponent(): PropertiesComponent = PropertiesComponent.getInstance()

val NumberOfForwardLines: Int
    get() = propertiesComponent().getInt("JumpingLines.NumberOfFLines", 4)

val NumberOfBackwardLines: Int
    get() = propertiesComponent().getInt("JumpingLines.NumberOfBLines", 2)

val jumpScore: Long
    get() = propertiesComponent().getLong("JumpingLines.JumpScore", 0)

fun Long.toHumanReadableForm(): String {
    return when {
        this >= 1000000000 -> String.format("%.2fB", this / 1000000000.0)
        this >= 1000000 -> String.format("%.2fM", this / 1000000.0)
        this >= 100000 -> String.format("%.2fL", this / 100000.0)
        this >= 1000 -> String.format("%.2fK", this / 1000.0)
        else -> this.toString()
    }
}

fun increaseJumpScoreBy(score: Int) {
    propertiesComponent().setValue("JumpingLines.JumpScore", (jumpScore + score).toString())
}

fun getTargetOffsetForward(document: Document, foldingModel: FoldingModel, currentOffset: Int, linesToJump: Int): Int {

    var linesJumped = 0
    var targetOffset = currentOffset

    foldingModel.runBatchFoldingOperation {
        while (linesJumped < linesToJump) {
            val currentLineNumber = document.getLineNumber(targetOffset)
            val newLineNumber = currentLineNumber + 1

            if (newLineNumber >= document.lineCount)
                break

            val newOffset = document.getLineStartOffset(newLineNumber)
            val foldRegion = foldingModel.getCollapsedRegionAtOffset(newOffset)

            if (foldRegion != null) {
                // Skip the folded region
                targetOffset = foldRegion.endOffset

                // If the folded region ends at the last line of the document, stop jumping
                if (document.getLineNumber(targetOffset) >= document.lineCount - 1)
                    break

            } else {
                // move to the next line
                targetOffset = newOffset
                linesJumped++
            }
        }
    }

    return targetOffset
}

fun getTargetOffsetBackward(document: Document, foldingModel: FoldingModel, currentOffset: Int, linesToJump: Int): Int {

    var linesJumped = 0
    var targetOffset = currentOffset

    foldingModel.runBatchFoldingOperation {
        while (linesJumped < linesToJump) {
            val currentLineNumber = document.getLineNumber(targetOffset)
            val newLineNumber = currentLineNumber - 1

            if (newLineNumber < 0)
                break

            val newOffset = document.getLineEndOffset(newLineNumber)
            val foldRegion = foldingModel.getCollapsedRegionAtOffset(newOffset)

            if (foldRegion != null) {
                // Skip the folded region
                targetOffset = foldRegion.startOffset

                // If the folded region ends at the last line of the document, stop jumping
                if (document.getLineNumber(targetOffset) <= 0)
                    break

            } else {
                // move to the next line
                targetOffset = newOffset
                linesJumped++
            }
        }
    }

    return targetOffset
}