package com.harsh.jumpinglines.jumps.middle

import com.harsh.jumpinglines.notification.showNotification
import com.harsh.jumpinglines.utils.editor
import com.harsh.jumpinglines.utils.updateJumpScore
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.editor.*
import com.intellij.openapi.project.DumbAwareAction

class JumpOnMiddle : DumbAwareAction() {
    override fun actionPerformed(event: AnActionEvent) {

        event.project ?: return

        try {

            val editor: Editor = event.editor
            val scrollingModel: ScrollingModel = editor.scrollingModel
            val selectionModel: SelectionModel = editor.selectionModel
            val caretModel: CaretModel = editor.caretModel
            val document: Document = editor.document
            val currentOffset = caretModel.offset

            // Calculate the first visible visual line
            val firstVisibleVisualLine = editor.yToVisualLine(scrollingModel.verticalScrollOffset)

            // Calculate the number of visible lines in the editor's visible area
            val visibleAreaHeight = scrollingModel.visibleArea.height
            val visibleLineCount = visibleAreaHeight / editor.lineHeight

            val lastVisibleLine = firstVisibleVisualLine + visibleLineCount
            val middleVisibleLine = (firstVisibleVisualLine + lastVisibleLine) / 2

            // Calculate the middle visible line
            val middleVisibleLogicalLine =
                editor.visualToLogicalPosition(VisualPosition(middleVisibleLine, 0)).line

            // Move the cursor to the calculated middle line, considering to stay within document bounds
            val middleLineOffset =
                document.getLineStartOffset(middleVisibleLogicalLine.coerceIn(0, document.lineCount - 1))

            // Move cursor to the middle line.
            caretModel.moveToOffset(middleLineOffset)

            // Remove selection blocks before jumping (if any).
            if (selectionModel.hasSelection()) {
                selectionModel.removeSelection(/* allCarets = */ true)
            }

            // Remove secondary cursors if any.
            if (caretModel.caretCount > 1) {
                caretModel.removeSecondaryCarets()
            }

            updateJumpScore(document, currentOffset, middleLineOffset)

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