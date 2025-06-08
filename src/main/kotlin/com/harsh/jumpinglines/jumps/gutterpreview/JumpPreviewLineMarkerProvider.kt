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
        val document = PsiDocumentManager.getInstance(project).getDocument(element.containingFile) ?: return null

        val offset = element.textRange.startOffset
        val lineNumber = document.getLineNumber(offset)

        val state = project.service<JumpLineStateService>()
        if (lineNumber != state.forwardLine && lineNumber != state.backwardLine) return null

        return LineMarkerInfo(
            /* element = */ element,
            /* range = */ element.textRange,
            /* icon = */ AllIcons.General.ModifiedSelected,
            /* tooltipProvider = */ { "Jump marker on line $lineNumber" },
            /* navHandler = */ null,
            /* alignment = */ GutterIconRenderer.Alignment.LEFT,
            /* accessibleNameProvider = */ { "Future jump to line $lineNumber" }
        )
    }

}