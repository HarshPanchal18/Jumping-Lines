package com.harsh.jumpinglines.settings

import com.harsh.jumpinglines.utils.Const
import com.harsh.jumpinglines.utils.Jumper.NumberOfBackwardLines
import com.harsh.jumpinglines.utils.Jumper.NumberOfForwardLines
import com.harsh.jumpinglines.utils.properties
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.options.Configurable
import javax.swing.JComponent

class JumpingLinesSettings : Configurable {

    private val pluginSettingPanel = JumpingLinesSettingPanel()
    private var savedNumberOfForwardLines: Int = 0 // Default to 0
    private var savedNumberOfBackwardLines: Int = 0

    override fun createComponent(): JComponent = pluginSettingPanel.parentPanel

    override fun isModified(): Boolean {
        return pluginSettingPanel.getForwardLinesValue() != savedNumberOfForwardLines ||
                pluginSettingPanel.getBackwardLinesValue() != savedNumberOfBackwardLines
    }

    override fun apply() {
        val newForwardNumberOfLines: Int = pluginSettingPanel.getForwardLinesValue()
        val newBackwardNumberOfLines: Int = pluginSettingPanel.getBackwardLinesValue()

        ApplicationManager.getApplication().runWriteAction {
            // Set value or unset if equals to default value
            properties().setValue(Const.FORWARD_LINES, newForwardNumberOfLines, 4)
            properties().setValue(Const.BACKWARD_LINES, newBackwardNumberOfLines, 2)
        }

        // Save the value to plugin's settings
        savedNumberOfForwardLines = newForwardNumberOfLines
        savedNumberOfBackwardLines = newBackwardNumberOfLines
    }

    override fun getDisplayName(): String = Const.JUMPING_LINES_SETTINGS // Display name in the settings dialog

    override fun reset() {
        // Set default values
        pluginSettingPanel.setNumberOfLines(NumberOfForwardLines, NumberOfBackwardLines)
        savedNumberOfForwardLines = NumberOfForwardLines
        savedNumberOfBackwardLines = NumberOfBackwardLines
    }
}