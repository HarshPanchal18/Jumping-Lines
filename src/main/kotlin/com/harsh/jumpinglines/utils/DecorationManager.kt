package com.harsh.jumpinglines.utils

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.EditorCustomElementRenderer
import com.intellij.openapi.editor.Inlay
import com.intellij.openapi.editor.markup.TextAttributes
import com.intellij.util.Alarm
import java.awt.*

object DecorationManager {

    // Store inlays and marks per editor
    private val editorInlays = mutableMapOf<Editor, MutableList<Inlay<*>>>()
    private val editorLineMarks = mutableMapOf<Editor, MutableSet<Int>>()

    fun addDecorationForCaret(editor: Editor, offset: Int, guideColor: Color) {

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
                    val g2d = g as Graphics2D  // Cast to Graphics2D to access alpha settings

                    // Set opacity (alpha value between 0.0f = transparent and 1.0f = opaque)
                    val alpha = 0.6f  // 60% opacity
                    val originalComposite = g2d.composite
                    g2d.composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha)

                    g2d.color = guideColor
                    g2d.fillOval(targetRegion.x, targetRegion.y + targetRegion.height / 2 - 3, 6, 6)

                    // Restore original composite so it doesn’t affect other painting
                    g2d.composite = originalComposite
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

    private val alarm = Alarm(Alarm.ThreadToUse.SWING_THREAD)

    fun scheduleClearMarkers(editor: Editor, delayMillis: Int = 5000) {
        alarm.cancelAllRequests()
        alarm.addRequest({
            clearAllDecorations(editor)
        }, delayMillis)
    }

}