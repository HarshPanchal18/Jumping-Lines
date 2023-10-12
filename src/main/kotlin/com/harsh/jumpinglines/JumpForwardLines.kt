package com.harsh.jumpinglines

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.editor.CaretModel
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.LogicalPosition
import com.intellij.openapi.editor.ScrollType
import com.intellij.openapi.editor.ScrollingModel

class JumpForwardLines : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {

        if (e.project == null)
            return

        val editor: Editor = e.getRequiredData(CommonDataKeys.EDITOR)
        val document: Document = editor.document
        val caretModel: CaretModel = editor.caretModel
        val currentOffset: Int = caretModel.offset
        val scrollingModel: ScrollingModel = editor.scrollingModel

        val linesToJump = 4 // Lines to jump, Positive for down, Negative for up

        // Calculate the new caret position
        val currentLineNumber: Int = document.getLineNumber(currentOffset)
        val newLineNumber: Int = currentLineNumber + linesToJump

        // Ensure the new line number is within valid bounds
        val validLineNumber: Int = newLineNumber.coerceIn(0, document.lineCount - 1)
        /*val validLineNumber = when {
            newLineNumber < 0 -> 0
            newLineNumber >= document.lineCount -> document.textLength - 1
            else -> document.getLineStartOffset(newLineNumber)
        }*/
        val newOffset: Int = document.getLineStartOffset(validLineNumber)
        caretModel.moveToOffset(newOffset)

        // Scrolling editor along with the cursor
        val newPosition = LogicalPosition(newLineNumber, 0)
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