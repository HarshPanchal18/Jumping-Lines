package com.harsh.jumpinglines.notification

import com.harsh.jumpinglines.utils.Const
import com.harsh.jumpinglines.utils.getPluginVersion
import com.harsh.jumpinglines.utils.properties
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.options.ShowSettingsUtil
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity
import com.intellij.openapi.updateSettings.impl.pluginsAdvertisement.notificationGroup

class PluginUpdateNotifier : StartupActivity {
    override fun runActivity(project: Project) {

        val previousKnownVersion = properties().getValue(Const.PREVIOUS_KNOWN_VERSION)
        val currentVersion = getPluginVersion()

        if (previousKnownVersion != currentVersion) {
            val notification = notificationGroup.createNotification(
                title = "Jumping Lines",
                content = "<b>Plugin update installed.</b><br/>" +
                        "<b><a href=\"https://jumpinglines.com\">Click here</a> to know your jump score.</b>",
                type = NotificationType.INFORMATION,
            ).setListener { notification, event ->
                if (event.description.contains("https://jumpinglines.com")) {
                    notification.hideBalloon()
                    ShowSettingsUtil.getInstance().showSettingsDialog(project, "Jumping Lines")
                }
            }

            Notifications.Bus.notify(notification)

            properties().setValue(Const.PREVIOUS_KNOWN_VERSION, currentVersion)
        }

    }
}