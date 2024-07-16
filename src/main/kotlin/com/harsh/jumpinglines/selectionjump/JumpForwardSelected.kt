package com.harsh.jumpinglines.selectionjump

import com.harsh.jumpinglines.notification.showNotification
import com.harsh.jumpinglines.utils.currentForwardNoOfLines
import com.harsh.jumpinglines.utils.editor
import com.harsh.jumpinglines.utils.getTargetOffsetForward
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.editor.*
import com.intellij.openapi.project.DumbAwareAction

class JumpForwardSelected : DumbAwareAction() {

	override fun actionPerformed(event: AnActionEvent) {

		event.project ?: return

		try {

			val editor: Editor = event.editor
			val document: Document = editor.document
			val caretModel: CaretModel = editor.caretModel
			val currentOffset: Int = caretModel.offset
			val scrollingModel: ScrollingModel = editor.scrollingModel
			val selectionModel: SelectionModel = editor.selectionModel
			val foldingModel: FoldingModel = editor.foldingModel

			// If there is a selection, start from the beginning of the selection; otherwise, start from the current caret position
			val startOffset: Int =
				if (selectionModel.hasSelection()) selectionModel.leadSelectionOffset else currentOffset

			// Calculate the new caret position
			val targetOffset = getTargetOffsetForward(document, foldingModel, currentOffset, currentForwardNoOfLines)

			caretModel.moveToOffset(targetOffset)

			// extend the selection
			selectionModel.setSelection(/* startOffset = */ startOffset, /* endOffset = */ caretModel.offset)

			// Scrolling editor along with the cursor
			val newLineNumber = document.getLineNumber(targetOffset)
			val newPosition = LogicalPosition(/* line = */ newLineNumber, /* column = */ 0)
			caretModel.moveToLogicalPosition(newPosition)

			scrollingModel.scrollTo(newPosition, ScrollType.RELATIVE)

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