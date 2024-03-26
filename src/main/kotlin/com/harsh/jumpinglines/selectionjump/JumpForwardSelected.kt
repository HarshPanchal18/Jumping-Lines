package com.harsh.jumpinglines.selectionjump

import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.editor.*

class JumpForwardSelected : AnAction() {

	override fun actionPerformed(event: AnActionEvent) {

		event.project ?: return

		val editor: Editor = event.getRequiredData(CommonDataKeys.EDITOR)
		val document: Document = editor.document
		val caretModel: CaretModel = editor.caretModel
		val currentOffset: Int = caretModel.offset
		val scrollingModel: ScrollingModel = editor.scrollingModel
		val selectionModel: SelectionModel = editor.selectionModel

		val properties = PropertiesComponent.getInstance()
		val currentForwardNoOfLines =
			properties.getValue(/* name = */ "JumpLines.NumberOfFLines", /* defaultValue = */"4").toInt()

		// If there is a selection, start from the beginning of the selection; otherwise, start from the current caret position
		val startOffset: Int = if (selectionModel.hasSelection()) selectionModel.leadSelectionOffset else currentOffset

		// Calculate the new caret position
		val currentLineNumber: Int = document.getLineNumber(currentOffset)
		val newLineNumber: Int = currentLineNumber + currentForwardNoOfLines

		// Ensure the new line number is within valid bounds
		val validLineNumber: Int = newLineNumber.coerceIn(0, maximumValue = document.lineCount - 1)
		val newOffset: Int = document.getLineStartOffset(validLineNumber)
		caretModel.moveToOffset(newOffset)

		// extend the selection
		selectionModel.setSelection(/* startOffset = */ startOffset, /* endOffset = */ caretModel.offset)

		// Scrolling editor along with the cursor
		val newPosition = LogicalPosition(/* line = */ newLineNumber, /* column = */ 0)
		caretModel.moveToLogicalPosition(newPosition)

		scrollingModel.scrollTo(newPosition, ScrollType.RELATIVE)
	}

	override fun update(e: AnActionEvent) {
		val project = e.project
		e.presentation.isEnabled = project != null
	}

	override fun getActionUpdateThread(): ActionUpdateThread {
		return ActionUpdateThread.EDT
	}
}