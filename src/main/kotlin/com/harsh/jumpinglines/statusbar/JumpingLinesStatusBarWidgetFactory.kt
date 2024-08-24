package com.harsh.jumpinglines.statusbar

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.StatusBarWidget
import com.intellij.openapi.wm.StatusBarWidgetFactory

class JumpingLinesStatusBarWidgetFactory : StatusBarWidgetFactory {

    override fun getId(): String {
        return "JumpingLines"
    }

    override fun getDisplayName(): String {
        return "Jumping Lines score"
    }

    override fun isAvailable(project: Project) = true

    override fun createWidget(project: Project): StatusBarWidget {
        return JumpingLinesStatusBarWidget()
    }

    override fun disposeWidget(widget: StatusBarWidget) {
        widget.dispose()
    }

}