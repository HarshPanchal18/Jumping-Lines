package com.harsh.jumpinglines.notification

import com.harsh.jumpinglines.settings.JumpingLinesSettings
import com.harsh.jumpinglines.utils.Const
import com.harsh.jumpinglines.utils.getPluginVersion
import com.harsh.jumpinglines.utils.properties
import com.intellij.openapi.options.ShowSettingsUtil
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity

class PluginUpdateNotifier : StartupActivity {
    override fun runActivity(project: Project) {

        val previousKnownVersion = properties().getValue(Const.PREVIOUS_KNOWN_VERSION)
        val currentVersion = getPluginVersion()

        if (previousKnownVersion != currentVersion) {

            showNotification(
                message = "<b>New changes are applied.</b><br/>" +
                        "<b><a href=\"https://jumpinglines.com\">Click here</a> to know your jump score.</b>"
            ) { notification, event ->
                if (event?.description?.contains("https://jumpinglines.com") == true) {
                    notification?.hideBalloon()
                    ShowSettingsUtil.getInstance().showSettingsDialog(project, JumpingLinesSettings::class.java)
                }
            }

            properties().setValue(Const.PREVIOUS_KNOWN_VERSION, currentVersion)

        }
    }
}