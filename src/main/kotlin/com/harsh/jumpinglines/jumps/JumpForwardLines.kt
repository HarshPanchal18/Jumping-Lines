package com.harsh.jumpinglines.jumps

import com.harsh.jumpinglines.notification.showNotification
import com.harsh.jumpinglines.utils.NumberOfForwardLines
import com.harsh.jumpinglines.utils.editor
import com.harsh.jumpinglines.utils.getTargetOffsetForward
import com.harsh.jumpinglines.utils.increaseJumpScoreBy
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.editor.*
import com.intellij.openapi.project.DumbAwareAction

class JumpForwardLines : DumbAwareAction() {

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

            // Calculate the new caret position while skipping folded regions
            val targetOffset = getTargetOffsetForward(
                document = document,
                foldingModel = foldingModel,
                currentOffset = currentOffset,
                linesToJump = NumberOfForwardLines
            )

            // Calculate the new caret position
            val targetLineNumber: Int = document.getLineNumber(targetOffset)
            val caretColumn = caretModel.logicalPosition.column
            caretModel.moveToOffset(targetOffset)

            // Scrolling editor along with the cursor
            val newPosition = LogicalPosition(targetLineNumber, caretColumn)
            caretModel.moveToLogicalPosition(newPosition)

            // Remove selection blocks before jumping
            if (selectionModel.hasSelection())
                selectionModel.removeSelection(/* allCarets = */ true)

            scrollingModel.scrollTo(newPosition, ScrollType.RELATIVE)

            if (document.getLineNumber(currentOffset) != document.getLineNumber(targetOffset)) {
                val score = document.getLineNumber(targetOffset) - document.getLineNumber(currentOffset)
                increaseJumpScoreBy(score)
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