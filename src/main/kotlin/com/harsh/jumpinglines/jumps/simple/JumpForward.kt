package com.harsh.jumpinglines.jumps.simple

import com.harsh.jumpinglines.notification.showNotification
import com.harsh.jumpinglines.utils.*
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.DumbAwareAction

class JumpForward : DumbAwareAction() {

    override fun actionPerformed(event: AnActionEvent) {

        event.project ?: return

        try {

            val editor: Editor = event.editor
            val currentOffset: Int = editor.caretModel.offset

            // Calculate the new caret position while skipping folded regions
            val targetOffset = Jumper.jumpForwardPreservingFolds(editor)

            Jumper.moveCaretAndScroll(editor = editor, toOffset = targetOffset)

            if (properties().getBoolean(Const.IS_SOUND_ENABLED)) {
                SoundPlayer.playJumpSound()
            }

            Jumper.updateJumpScore(document = editor.document, fromOffset = currentOffset, toOffset = targetOffset)

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