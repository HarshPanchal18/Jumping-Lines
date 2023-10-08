package com.example.jumpinglines

import com.intellij.openapi.options.Configurable
import javax.swing.JComponent

class JumpLinesSettings : Configurable {

    private val settingsPanel = JumpLinesSettingPanel()
    private var savedNumberOfLines: Int = -1//settingsPanel.getNumberOfLines()

    override fun createComponent(): JComponent {
        return settingsPanel.panel
    }

    override fun isModified(): Boolean {
        return settingsPanel.getNumberOfLines() != savedNumberOfLines
    }

    override fun apply() {
        // Apply the settings to your plugin logic
        val numberOfLines = settingsPanel.getNumberOfLines()
        // Save the value to your plugin's settings
        savedNumberOfLines = numberOfLines
    }

    override fun getDisplayName(): String {
        return "Jump K Lines Settings" // Display name in the settings dialog
    }

    override fun reset() {
        val currentNumberOfLines = settingsPanel.getNumberOfLines()
        savedNumberOfLines = currentNumberOfLines
        settingsPanel.setNumberOfLines(currentNumberOfLines)
    }
}