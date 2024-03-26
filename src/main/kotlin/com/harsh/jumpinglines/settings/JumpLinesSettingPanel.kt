package com.harsh.jumpinglines.settings

import com.intellij.ui.components.panels.VerticalLayout
import java.awt.FlowLayout
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextField
import javax.swing.SwingConstants

class JumpLinesSettingPanel {

	val parentPanel: JPanel
	private var forwardRowLayout: JPanel
	private var backwardRowLayout: JPanel
	private val forwardLineTextField = JTextField()
	private val backwardLineTextField = JTextField()

	init {
		val fwdRow = FlowLayout(FlowLayout.LEFT, /* hgap = */ 5, /* vgap = */ 2)
		val forwardLabel = JLabel(/* text = */ "Number of lines to jump forward:   ")
		forwardRowLayout = JPanel(fwdRow).apply {
			add(forwardLabel)
			add(forwardLineTextField)
		}

		val bwdRow = FlowLayout(FlowLayout.LEFT, /* hgap = */ 5, /* vgap = */ 2)
		val backwardLabel = JLabel(/* text = */ "Number of lines to jump backward:")
		backwardRowLayout = JPanel(bwdRow).apply {
			add(backwardLabel)
			add(backwardLineTextField)
		}

		val layout = VerticalLayout(/* gap = */ 2,/* alignment = */ SwingConstants.LEFT)
		parentPanel = JPanel(layout).apply {
			add(forwardRowLayout)
			add(backwardRowLayout)
		}
	}

	fun getForwardLines(): Int {
		val textFieldValue = forwardLineTextField.text
		return textFieldValue.filter { it.isDigit() }.toInt()
	}

	fun getBackwardLines(): Int {
		val textFieldValue = backwardLineTextField.text
		return textFieldValue.filter { it.isDigit() }.toInt()
	}

	fun setNumberOfLines(forwardLine: Int, backwardLine: Int) {
		forwardLineTextField.text = forwardLine.toString()
		backwardLineTextField.text = backwardLine.toString()
	}

}