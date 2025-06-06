package com.harsh.jumpinglines.statusbar

import com.harsh.jumpinglines.utils.Const
import com.harsh.jumpinglines.utils.Jumper.jumpScore
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.StatusBar
import com.intellij.openapi.wm.StatusBarWidget
import com.intellij.openapi.wm.StatusBarWidgetFactory

class JumpingLinesStatusBarWidgetFactory : StatusBarWidgetFactory {

    override fun getId(): String = Const.PLUGIN_ID

    override fun canBeEnabledOn(statusBar: StatusBar): Boolean = true

    override fun getDisplayName(): String = Const.JUMP_SCORE_TITLE + ": " + jumpScore

    override fun isAvailable(project: Project) = true

    override fun createWidget(project: Project): StatusBarWidget = JumpingLinesStatusBarWidget(project)

    override fun disposeWidget(widget: StatusBarWidget) {
        widget.dispose()
    }

}