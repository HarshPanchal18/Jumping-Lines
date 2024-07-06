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

val currentForwardNoOfLines: Int
	get() = propertiesComponent().getInt("JumpLines.NumberOfFLines", 4)

val currentBackwardNoOfLines: Int
	get() = propertiesComponent().getInt("JumpLines.NumberOfBLines", 2)

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