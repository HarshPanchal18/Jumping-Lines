package com.harsh.jumpinglines.statusbar

import com.harsh.jumpinglines.utils.Const
import com.harsh.jumpinglines.utils.inHumanReadableForm
import com.harsh.jumpinglines.utils.jumpScore
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.wm.StatusBar
import com.intellij.openapi.wm.StatusBarWidget
import java.awt.Component
import java.util.*

class JumpingLinesStatusBarWidget : StatusBarWidget,
//    IconLikeCustomStatusBarWidget
//    StatusBarWidget.IconPresentation,
    StatusBarWidget.TextPresentation {

    private val timer = Timer()

    init {
        startTimer()
    }

    private var statusBar: StatusBar? = null

    override fun ID(): String = Const.PLUGIN_ID

//    override fun getIcon(): Icon = IconLoader.getIcon("/META-INF/pluginIcon16.svg", javaClass)

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

    // Method to refresh the widget
    fun refreshWidget() {
        statusBar?.updateWidget(ID())
    }

    private fun startTimer() {
        timer.scheduleAtFixedRate(
            object : TimerTask() {
                override fun run() {
                    refreshWidget()
                }
            },
            /* delay = */ 0, // delay in milliseconds before task is to be executed.
            /* period = */ 800 // check every 800 milliseconds
        )
    }

}