package com.harsh.jumpinglines.settings

import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.options.Configurable
import javax.swing.JComponent

class JumpLinesSettings : Configurable {

    private val settingsPanel = JumpLinesSettingPanel()
    private var savedNumberOfFLines: Int = 0
    private var savedNumberOfBLines: Int = 0

    override fun createComponent(): JComponent {
        return settingsPanel.panel
    }

    override fun isModified(): Boolean {
        return settingsPanel.getForwardLines() != savedNumberOfFLines || settingsPanel.getBackwardLines() != savedNumberOfBLines
    }

    override fun apply() {
        // Apply the settings to your plugin logic
        val forwardNumberOfLines = settingsPanel.getForwardLines()
        val backwardNumberOfLines = settingsPanel.getBackwardLines()

        ApplicationManager.getApplication().runWriteAction {
            val properties = PropertiesComponent.getInstance()
            properties.setValue("JumpLines.NumberOfFLines", forwardNumberOfLines.toString())
            properties.setValue("JumpLines.NumberOfBLines", backwardNumberOfLines.toString())
        }

        // Save the value to your plugin's settings
        savedNumberOfFLines = forwardNumberOfLines
        savedNumberOfBLines = backwardNumberOfLines
    }

    override fun getDisplayName(): String {
        return "Jumping Lines Settings" // Display name in the settings dialog
    }

    override fun reset() {
        val properties = PropertiesComponent.getInstance()
        val currentForwardNoOfLines = properties.getValue("JumpLines.NumberOfFLines", "4").toInt()
        val currentBackwardNoOfLines = properties.getValue("JumpLines.NumberOfBLines", "2").toInt()

        settingsPanel.setNumberOfLines(currentForwardNoOfLines, currentBackwardNoOfLines)

        savedNumberOfFLines = currentForwardNoOfLines
        savedNumberOfBLines = currentBackwardNoOfLines
    }
}