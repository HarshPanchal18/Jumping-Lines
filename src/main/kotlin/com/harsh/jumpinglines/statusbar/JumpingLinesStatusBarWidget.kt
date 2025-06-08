package com.harsh.jumpinglines.statusbar

import com.harsh.jumpinglines.settings.JumpingLinesSettings
import com.harsh.jumpinglines.utils.Const
import com.harsh.jumpinglines.utils.Jumper.jumpScore
import com.harsh.jumpinglines.utils.inHumanReadableForm
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.options.ShowSettingsUtil
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.wm.IconLikeCustomStatusBarWidget
import com.intellij.openapi.wm.StatusBar
import com.intellij.openapi.wm.StatusBarWidget
import com.intellij.util.Consumer
import java.awt.Component
import java.awt.event.MouseEvent
import java.util.*
import javax.swing.JComponent
import javax.swing.JLabel

class JumpingLinesStatusBarWidget(
    private val project: Project
) : StatusBarWidget,
    IconLikeCustomStatusBarWidget,
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

    override fun getComponent(): JComponent {
        return JLabel().apply {
            /*icon = runCatching {
                IconLoader.getIcon("/icons/statusbarIcon.svg", JumpingLinesStatusBarWidget::class.java)
            }.getOrElse {
                AllIcons.Nodes.ExceptionClass
            }*/
            text = jumpScore.inHumanReadableForm()
            toolTipText = "Total lines jumped with Jumping Lines"
            iconTextGap = 4
        }
    }

    override fun getClickConsumer(): Consumer<MouseEvent> {
        return Consumer {
            ApplicationManager.getApplication().invokeLater {
                ShowSettingsUtil.getInstance().showSettingsDialog(project, JumpingLinesSettings::class.java)
            }
        }
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