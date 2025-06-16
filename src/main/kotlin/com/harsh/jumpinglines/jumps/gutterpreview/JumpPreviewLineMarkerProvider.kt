package com.harsh.jumpinglines.jumps.gutterpreview

import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProvider
import com.intellij.icons.AllIcons
import com.intellij.openapi.components.service
import com.intellij.openapi.editor.markup.GutterIconRenderer
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiElement

class JumpPreviewLineMarkerProvider : LineMarkerProvider {

    override fun getLineMarkerInfo(element: PsiElement): LineMarkerInfo<*>? {
        val project = element.project

        // Avoid non-leaf elements
        if (element.firstChild != null) return null

        val document = PsiDocumentManager.getInstance(project).getDocument(element.containingFile) ?: return null

        val offset = element.textRange.startOffset
        val lineNumber = document.getLineNumber(offset)

        // Only one icon per line — first leaf element
        val lineStartOffset = document.getLineStartOffset(lineNumber)
//        if (offset != lineStartOffset) return null
        if (offset < lineStartOffset || offset >= document.getLineEndOffset(lineNumber)) return null

        val state = project.service<JumpLineStateService>()
        println("Checking line: $lineNumber | forward=${state.forwardLine}, backward=${state.backwardLine}")

        if (lineNumber != state.forwardLine && lineNumber != state.backwardLine) {
            println("MATCH on line $lineNumber | element: ${element.text}")
            return null
        }

        val icon = when (lineNumber) {
            state.forwardLine -> AllIcons.General.InlineAdd
            state.backwardLine -> AllIcons.General.Remove
            else -> null
        } ?: return null

        println("ICON $icon")

        return LineMarkerInfo(
            /* element = */ element,
            /* range = */ element.textRange,
            /* icon = */ icon,
            /* tooltipProvider = */ { null },
            /* navHandler = */ null,
            /* alignment = */ GutterIconRenderer.Alignment.RIGHT,
            /* accessibleNameProvider = */ { "" }
        )
    }

}