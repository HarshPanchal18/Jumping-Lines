package com.harsh.jumpinglines.middle

import com.harsh.jumpinglines.notification.showNotification
import com.harsh.jumpinglines.utils.editor
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.editor.*

class JumpOnMiddle : AnAction() {
	override fun actionPerformed(event: AnActionEvent) {

		event.project ?: return

		try {

			val editor: Editor = event.editor
			val scrollingModel: ScrollingModel = editor.scrollingModel
			val selectionModel: SelectionModel = editor.selectionModel
			val caretModel: CaretModel = editor.caretModel
			val document: Document = editor.document

			// Get vertical scroll offset and line height of editor
			val verticalScrollOffset = scrollingModel.verticalScrollOffset
			val lineHeight = editor.lineHeight

			// Calculate the first visible line
			val firstVisibleLine = verticalScrollOffset / lineHeight

			// Calculate the number of visible lines in the editor's visible area
			val visibleAreaHeight = scrollingModel.visibleArea.height
			val visibleLineCount = visibleAreaHeight / lineHeight

			val lastVisibleLine = firstVisibleLine + visibleLineCount

			// Calculate the middle visible line
			val middleVisibleLine = (firstVisibleLine + lastVisibleLine) / 2

			// Move the cursor to the calculated middle line, considering to stay within document bounds
			val middleLineOffset = document.getLineStartOffset(middleVisibleLine.coerceIn(0, document.lineCount - 1))
			caretModel.moveToOffset(middleLineOffset)

			// Remove selection blocks before jumping
			if (selectionModel.hasSelection()) {
				selectionModel.removeSelection(/* allCarets = */ true)
			}

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