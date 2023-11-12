package com.harsh.jumpinglines.settings

import com.intellij.ui.components.panels.VerticalLayout
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextField

class JumpLinesSettingPanel {

    val panel: JPanel
    private val numberOfLinesForwardTextField = JTextField()
    private val numberOfLinesBackwardTextField = JTextField()

    init {
        val layout = VerticalLayout(2)
        panel = JPanel(layout)

        val forwardLabel = JLabel("Number of lines to jump forward:")
        panel.add(forwardLabel)
        panel.add(numberOfLinesForwardTextField)

        val backwardLabel = JLabel("Number of lines to jump backward:")
        panel.add(backwardLabel)
        panel.add(numberOfLinesBackwardTextField)
    }

    fun getForwardLines(): Int {
        val textFieldValue = numberOfLinesForwardTextField.text
        return textFieldValue.filter { it.isDigit() }.toInt()
    }

    fun getBackwardLines(): Int {
        val textFieldValue = numberOfLinesBackwardTextField.text
        return textFieldValue.filter { it.isDigit() }.toInt()
    }

    fun setNumberOfLines(forwardLine: Int, backwardLine: Int) {
        numberOfLinesForwardTextField.text = forwardLine.toString()
        numberOfLinesBackwardTextField.text = backwardLine.toString()
    }

}