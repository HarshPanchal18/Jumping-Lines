package com.harsh.jumpinglines.settings

import com.harsh.jumpinglines.utils.inHumanReadableForm
import com.harsh.jumpinglines.utils.jumpScore
import com.intellij.icons.AllIcons
import com.intellij.ui.components.panels.VerticalLayout
import java.awt.FlowLayout
import javax.swing.*

class JumpingLinesSettingPanel {

    val parentPanel: JPanel
    private var forwardJumpRow: JPanel
    private var backwardJumpRow: JPanel
    private var scoreRow: JPanel
    private var scoreLabel: JLabel
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

        val forwardJumpLayout = FlowLayout(/* align = */ FlowLayout.LEFT, /* hgap = */ 5, /* vgap = */ 2)
        forwardJumpRow = JPanel(forwardJumpLayout).apply {
            add(JLabel(AllIcons.General.ArrowDown))
            val forwardLabel = "<html>Number of lines to jump <b>forward:" + "&nbsp;".repeat(3) + "</b></html>"
            add(JLabel(forwardLabel))
            add(forwardLineSpinner)
        }

        val backwardJumpLayout = FlowLayout(/* align = */ FlowLayout.LEFT, /* hgap = */ 5, /* vgap = */ 2)
        backwardJumpRow = JPanel(backwardJumpLayout).apply {
            add(JLabel(AllIcons.General.ArrowUp))
            val backwardLabel = "<html>Number of lines to jump <b>backward:</b></html>"
            add(JLabel(backwardLabel))
            add(backwardLineSpinner)
        }

        val jumpScoreLayout = FlowLayout(/* align = */ FlowLayout.LEFT, /* hgap = */ 5, /* vgap = */ 2)
        scoreLabel =
            JLabel(
                "<html>" +
                        "Your progress is <b>remarkable!</b> " +
                        "You've jumped over <b>~${jumpScore.inHumanReadableForm()}</b> lines." +
                        "</html>"
            ).apply { icon = AllIcons.General.GreenCheckmark }

        scoreRow = JPanel(jumpScoreLayout).apply {
            add(scoreLabel)
        }


        val parentLayout = VerticalLayout(/* gap = */ 2,/* alignment = */ SwingConstants.LEFT)
        parentPanel = JPanel(parentLayout).apply {
            add(backwardJumpRow)
            add(forwardJumpRow)
            add(scoreRow)
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