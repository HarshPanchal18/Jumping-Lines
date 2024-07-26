package com.harsh.jumpinglines.settings

import com.harsh.jumpinglines.utils.jumpScore
import com.harsh.jumpinglines.utils.propertiesComponent
import com.harsh.jumpinglines.utils.toHumanReadableForm
import com.intellij.icons.AllIcons
import com.intellij.ui.components.panels.VerticalLayout
import java.awt.FlowLayout
import javax.swing.*

class JumpLinesSettingPanel {

    val parentPanel: JPanel
    private var forwardJumpRow: JPanel
    private var backwardJumpRow: JPanel
    private var scoreRow: JPanel
    private var scoreLabel: JLabel
    private var scoreResetButton: JButton
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
            add(JLabel(/* text = */ "Number of lines to jump forward:   "))
            add(forwardLineSpinner)
        }

        val backwardJumpLayout = FlowLayout(/* align = */ FlowLayout.LEFT, /* hgap = */ 5, /* vgap = */ 2)
        backwardJumpRow = JPanel(backwardJumpLayout).apply {
            add(JLabel(/* text = */ "Number of lines to jump backward:"))
            add(backwardLineSpinner)
        }

        val jumpScoreLayout = FlowLayout(/* align = */ FlowLayout.LEFT, /* hgap = */ 5, /* vgap = */ 2)
        scoreLabel =
            JLabel(
                "<html>" +
                        "Your progress is <b>remarkable!</b> " +
                        "You've jumped <b>~${jumpScore.toHumanReadableForm()}</b> lines." +
                        "</html>"
            ) //.apply { icon = AllIcons.General.Information }

        scoreResetButton = JButton("Reset").apply {
            icon = AllIcons.Diff.Revert
            iconTextGap = 5
            addActionListener {
                propertiesComponent().setValue("JumpingLines.JumpScore", "0")
                scoreLabel.text = "<html>" +
                        "Your progress is <b>remarkable!</b> " +
                        "You've jumped <b>~${jumpScore.toHumanReadableForm()}</b> lines." +
                        "</html>"
            }
        }

        scoreRow = JPanel(jumpScoreLayout).apply {
            add(scoreLabel)
            add(scoreResetButton)
        }

        val layout = VerticalLayout(/* gap = */ 2,/* alignment = */ SwingConstants.LEFT)
        parentPanel = JPanel(layout).apply {
            add(forwardJumpRow)
            add(backwardJumpRow)
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