package com.example.jumpinglines

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.editor.Editor

class JumpLines : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project
        val editor: Editor = e.getRequiredData(CommonDataKeys.EDITOR)

        if (project == null)
            return

        val caretModel = editor.caretModel
        val currentOffset = caretModel.offset
        val linesToJump = 3 // Lines to jump, Positive for down, Negative for up

        // Calculate the new caret position
        val document = editor.document
        val lineCount = document.lineCount
        val currentLineNumber = document.getLineNumber(currentOffset)
        val newLineNumber = currentLineNumber + linesToJump

        // Ensure the new line number is within valid bounds
        val validLineNumber = newLineNumber.coerceIn(0, lineCount - 1)
        val newOffset = document.getLineStartOffset(validLineNumber)

        caretModel.moveToOffset(newOffset)
    }

    override fun update(e: AnActionEvent) {
        val project = e.project
        e.presentation.isEnabled = project!=null
    }

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.EDT
    }
}