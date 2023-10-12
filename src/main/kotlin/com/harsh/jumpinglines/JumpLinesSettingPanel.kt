package com.harsh.jumpinglines

import java.awt.BorderLayout
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextField

class JumpLinesSettingPanel {

    val panel = JPanel()
    private val numberOfLinesTextField = JTextField(5)

    init {
        panel.layout = BorderLayout()
        val label = JLabel("Number of lines to jump:")
        panel.add(label, BorderLayout.WEST)
        panel.add(numberOfLinesTextField, BorderLayout.CENTER)
    }

    fun getNumberOfLines(): Int {
        return numberOfLinesTextField.text.toIntOrNull() ?: 3
    }

    fun setNumberOfLines(lines: Int) {
        numberOfLinesTextField.text = lines.toString()
    }

}