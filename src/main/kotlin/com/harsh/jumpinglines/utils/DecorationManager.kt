package com.harsh.jumpinglines.utils

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.EditorCustomElementRenderer
import com.intellij.openapi.editor.Inlay
import com.intellij.openapi.editor.markup.HighlighterLayer
import com.intellij.openapi.editor.markup.HighlighterTargetArea
import com.intellij.openapi.editor.markup.RangeHighlighter
import com.intellij.openapi.editor.markup.TextAttributes
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

    fun flashLine(editor: Editor, offset: Int, guideColor: Color, durationInMs: Int = 5000) {

        // keep a set of line numbers that are already flashing so we don’t stack multiple highlighters on a single line.
        val lineMarks = editorLineMarks.getOrPut(editor) { mutableSetOf() }
        val document = editor.document
        val line = document.getLineNumber(offset)

        // already flashing?  ──► do nothing
        if (lineMarks.contains(line)) return

        val start = document.getLineStartOffset(line)
        val end = document.getLineEndOffset(line)

        val attributes = TextAttributes().apply {
            backgroundColor = Color(guideColor.red, guideColor.green, guideColor.blue, (guideColor.alpha * 0.1).toInt())
        }

        val markup = editor.markupModel
        ApplicationManager.getApplication().runWriteAction<RangeHighlighter> {
            markup.addRangeHighlighter(
                start, end,
                HighlighterLayer.SELECTION - 1, // beneath selection, above default
                attributes,
                HighlighterTargetArea.LINES_IN_RANGE
            )
        }

        // Track the highlight so we can clear it later
        editorInlays.computeIfAbsent(editor) { mutableListOf() } // reuse same map
        lineMarks.add(line)

        // auto‑clear after duration
        scheduleTask(durationInMs) {
            markup.removeAllHighlighters()
        }
    }

    fun clearAllLineFlashes(editor: Editor) {
        editor.markupModel.removeAllHighlighters()
    }

}