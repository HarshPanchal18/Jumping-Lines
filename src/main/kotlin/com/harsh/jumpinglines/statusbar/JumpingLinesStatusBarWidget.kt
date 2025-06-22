package com.harsh.jumpinglines.statusbar

import com.harsh.jumpinglines.settings.JumpingLinesSettings
import com.harsh.jumpinglines.utils.Const
import com.harsh.jumpinglines.utils.Jumper.jumpScore
import com.harsh.jumpinglines.utils.inHumanReadableForm
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.options.ShowSettingsUtil
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.wm.StatusBar
import com.intellij.openapi.wm.StatusBarWidget
import com.intellij.util.Consumer
import java.awt.Component
import java.awt.event.MouseEvent
import java.util.*

class JumpingLinesStatusBarWidget(private val project: Project) : StatusBarWidget, StatusBarWidget.TextPresentation {

    private val timer = Timer()

    init {
        startTimer()
    }

    private var statusBar: StatusBar? = null

    override fun ID(): String = Const.PLUGIN_ID

    override fun getText(): String = jumpScore.inHumanReadableForm()

    override fun getTooltipText(): String = Const.PLUGIN_NAME

    override fun getAlignment(): Float = Component.CENTER_ALIGNMENT

    override fun getPresentation(): StatusBarWidget.WidgetPresentation = this

    override fun dispose() {
        Disposer.dispose(this)
        timer.cancel()
    }

    override fun install(statusBar: StatusBar) {
        this.statusBar = statusBar
    }

    // Open plugin settings on click over widget.
    override fun getClickConsumer(): Consumer<MouseEvent> {
        return Consumer {
            ApplicationManager.getApplication().invokeLater {
                ShowSettingsUtil.getInstance().showSettingsDialog(project, JumpingLinesSettings::class.java)
            }
        }
    }

    // Refresh the widget
    fun refreshWidget() {
        statusBar?.updateWidget(ID())
    }

    // Schedule a task to refresh widget in given period.
    private fun startTimer() {
        timer.scheduleAtFixedRate(
            /* task = */
            object : TimerTask() {
                override fun run() {
                    refreshWidget()
                }
            },
            /* delay = */ 0, // delay 0ms before task is to be executed. Start immediately.
            /* period = */ 800 // reflect change in 800ms.
        )
    }

}