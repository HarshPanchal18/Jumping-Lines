package com.harsh.jumpinglines.settings

import com.harsh.jumpinglines.utils.Const
import com.harsh.jumpinglines.utils.Jumper.IsMarkerEnabled
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
    private var isMarkerEnabled: Boolean = false

    override fun createComponent(): JComponent = pluginSettingPanel.parentPanel

    override fun isModified(): Boolean {
        return pluginSettingPanel.getForwardLinesValue() != savedNumberOfForwardLines ||
                pluginSettingPanel.getBackwardLinesValue() != savedNumberOfBackwardLines ||
                pluginSettingPanel.getMarkerState() != isMarkerEnabled
    }

    override fun apply() {
        ApplicationManager.getApplication().runWriteAction {
            // Set value or unset if equals to default value
            properties().setValue(Const.FORWARD_LINES, pluginSettingPanel.getForwardLinesValue(), 4)
            properties().setValue(Const.BACKWARD_LINES, pluginSettingPanel.getBackwardLinesValue(), 2)
            properties().setValue(Const.IS_MARKER_ENABLED, pluginSettingPanel.getMarkerState())
        }

        // Holding values, for smelling to modify().
        savedNumberOfForwardLines = pluginSettingPanel.getForwardLinesValue()
        savedNumberOfBackwardLines = pluginSettingPanel.getBackwardLinesValue()
        isMarkerEnabled = pluginSettingPanel.getMarkerState()
    }

    override fun getDisplayName(): String = Const.JUMPING_LINES_SETTINGS // Display name in the settings dialog

    override fun reset() {
        pluginSettingPanel.setValues(NumberOfForwardLines, NumberOfBackwardLines, IsMarkerEnabled)
    }
}