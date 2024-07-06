package com.harsh.jumpinglines.settings

import com.harsh.jumpinglines.utils.currentBackwardNoOfLines
import com.harsh.jumpinglines.utils.currentForwardNoOfLines
import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.options.Configurable
import javax.swing.JComponent

class JumpLinesSettings : Configurable {

	private val settingsPanel = JumpLinesSettingPanel()
	private var savedNumberOfFLines: Int = 0
	private var savedNumberOfBLines: Int = 0

	override fun createComponent(): JComponent {
		return settingsPanel.parentPanel
	}

	override fun isModified(): Boolean {
		return settingsPanel.getForwardLines() != savedNumberOfFLines ||
				settingsPanel.getBackwardLines() != savedNumberOfBLines
	}

	override fun apply() {
		val forwardNumberOfLines: Int = settingsPanel.getForwardLines()
		val backwardNumberOfLines: Int = settingsPanel.getBackwardLines()

		ApplicationManager.getApplication().runWriteAction {
			val properties = PropertiesComponent.getInstance()
			// Set value or unset if equals to default value
			properties.setValue("JumpLines.NumberOfFLines", forwardNumberOfLines, 4)
			properties.setValue("JumpLines.NumberOfBLines", backwardNumberOfLines, 2)
		}

		// Save the value to plugin's settings
		savedNumberOfFLines = forwardNumberOfLines
		savedNumberOfBLines = backwardNumberOfLines
	}

	override fun getDisplayName(): String {
		return "Jumping Lines Settings" // Display name in the settings dialog
	}

	override fun reset() {
		settingsPanel.setNumberOfLines(currentForwardNoOfLines, currentBackwardNoOfLines)
		savedNumberOfFLines = currentForwardNoOfLines
		savedNumberOfBLines = currentBackwardNoOfLines
	}
}