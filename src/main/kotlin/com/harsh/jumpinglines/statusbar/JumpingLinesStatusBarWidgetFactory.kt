package com.harsh.jumpinglines.statusbar

import com.harsh.jumpinglines.utils.Const
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.StatusBarWidget
import com.intellij.openapi.wm.StatusBarWidgetFactory

class JumpingLinesStatusBarWidgetFactory : StatusBarWidgetFactory {

    override fun getId(): String = Const.PLUGIN_ID

    override fun getDisplayName(): String = Const.JUMPING_LINES_SCORE

    override fun isAvailable(project: Project) = true

    override fun createWidget(project: Project): StatusBarWidget = JumpingLinesStatusBarWidget()

    override fun disposeWidget(widget: StatusBarWidget) {
        widget.dispose()
    }

}