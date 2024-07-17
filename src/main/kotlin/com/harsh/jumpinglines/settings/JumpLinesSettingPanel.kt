package com.harsh.jumpinglines.settings

import com.intellij.ui.components.panels.VerticalLayout
import java.awt.FlowLayout
import javax.swing.*

class JumpLinesSettingPanel {

    val parentPanel: JPanel
    private var forwardRowPanel: JPanel
    private var backwardRowPanel: JPanel
    private val forwardLineSpinner = JSpinner(
        SpinnerNumberModel(
            /* value = */ 0,
            /* minimum = */ 0,
            /* maximum = */ 50,
            /* stepSize = */ 1
        )
    )
    private val backwardLineSpinner = JSpinner(
        SpinnerNumberModel(
            /* value = */ 0,
            /* minimum = */ 0,
            /* maximum = */ 50,
            /* stepSize = */ 1
        )
    )

    init {
        val forwardRow = FlowLayout(/* align = */ FlowLayout.LEFT, /* hgap = */ 5, /* vgap = */ 2)
        val forwardLabel = JLabel(/* text = */ "Number of lines to jump forward:   ")

        forwardRowPanel = JPanel(forwardRow).apply {
            add(forwardLabel)
            add(forwardLineSpinner)
        }

        val backwardRow = FlowLayout(/* align = */ FlowLayout.LEFT, /* hgap = */ 5, /* vgap = */ 2)
        val backwardLabel = JLabel(/* text = */ "Number of lines to jump backward:")
        backwardRowPanel = JPanel(backwardRow).apply {
            add(backwardLabel)
            add(backwardLineSpinner)
        }

        val layout = VerticalLayout(/* gap = */ 2,/* alignment = */ SwingConstants.LEFT)
        parentPanel = JPanel(layout).apply {
            add(forwardRowPanel)
            add(backwardRowPanel)
        }
    }

    fun getForwardLines(): Int {
        return forwardLineSpinner.value as Int
    }

    fun getBackwardLines(): Int {
        return backwardLineSpinner.value as Int
    }

    fun setNumberOfLines(forwardLine: Int, backwardLine: Int) {
        forwardLineSpinner.value = forwardLine
        backwardLineSpinner.value = backwardLine
    }

}