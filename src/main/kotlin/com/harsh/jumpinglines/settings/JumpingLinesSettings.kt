package com.harsh.jumpinglines.settings

import com.harsh.jumpinglines.utils.Const
import com.harsh.jumpinglines.utils.NumberOfBackwardLines
import com.harsh.jumpinglines.utils.NumberOfForwardLines
import com.harsh.jumpinglines.utils.properties
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.options.Configurable
import javax.swing.JComponent

class JumpingLinesSettings : Configurable {

    private val pluginSettingPanel = JumpingLinesSettingPanel()
    private var savedNumberOfForwardLines: Int = 0
    private var savedNumberOfBackwardLines: Int = 0

    override fun createComponent(): JComponent = pluginSettingPanel.parentPanel

    override fun isModified(): Boolean {
        return pluginSettingPanel.getForwardLines() != savedNumberOfForwardLines ||
                pluginSettingPanel.getBackwardLines() != savedNumberOfBackwardLines
    }

    override fun apply() {
        val newForwardNumberOfLines: Int = pluginSettingPanel.getForwardLines()
        val newBackwardNumberOfLines: Int = pluginSettingPanel.getBackwardLines()

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