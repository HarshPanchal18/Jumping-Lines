package com.harsh.jumpinglines.jumps.cursortrace

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.EditorCustomElementRenderer
import com.intellij.openapi.editor.Inlay
import com.intellij.openapi.editor.markup.RangeHighlighter
import com.intellij.openapi.editor.markup.TextAttributes
import com.intellij.openapi.util.Key
import com.intellij.ui.JBColor
import java.awt.Graphics
import java.awt.Rectangle


object CaretReplicateDecorationManager {

    private val editorLineMarks = mutableMapOf<Editor, MutableSet<Int>>()

    // Store inlays per editor
    private val editorInlays = mutableMapOf<Editor, MutableList<Inlay<*>>>()

    private val decorations = mutableMapOf<Editor, MutableList<RangeHighlighter>>()

    private val CARET_CLEAR_LISTENER_ADDED_KEY = Key.create<Boolean>("jumpplugin.caret.clear.listener.added")

    fun addDecorationForCaret(editor: Editor, offset: Int) {

        val lineMarks = editorLineMarks.getOrPut(editor) { mutableSetOf() }

        // Decoration already exists for this line, do nothing
        val line = editor.document.getLineNumber(offset)
        if (lineMarks.contains(line)) return

        val inlay = ApplicationManager.getApplication().runWriteAction<Inlay<*>?> {
            editor.inlayModel.addInlineElement(offset, true, object : EditorCustomElementRenderer {

                override fun calcWidthInPixels(inlay: Inlay<*>): Int = 8

                override fun paint(
                    inlay: Inlay<*>,
                    g: Graphics,
                    targetRegion: Rectangle,
                    textAttributes: TextAttributes
                ) {
                    g.color = JBColor.RED
                    g.fillOval(targetRegion.x, targetRegion.y + targetRegion.height / 2 - 3, 6, 6)
                }
            })

        }

        if (inlay != null) {
            editorInlays.computeIfAbsent(editor) { mutableListOf() }.add(inlay)
            lineMarks.add(line)
        }

    }

    fun removeDecorationAtOffset(editor: Editor, offset: Int) {
        val inlays = editorInlays[editor] ?: return

        val iterator = inlays.iterator()
        while (iterator.hasNext()) {
            val inlay = iterator.next()
            if (inlay.offset == offset) {
                ApplicationManager.getApplication().runWriteAction {
                    inlay.dispose()
                }
                iterator.remove()
            }
        }
    }

    fun clearAllDecorations(editor: Editor) {
        val inlays = editorInlays.remove(editor) ?: return

        ApplicationManager.getApplication().runWriteAction {
            inlays.forEach { it.dispose() }
        }
        editorLineMarks.remove(editor)
    }

}