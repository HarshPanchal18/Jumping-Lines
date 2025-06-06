package com.harsh.jumpinglines.notification

import com.harsh.jumpinglines.settings.JumpingLinesSettings
import com.harsh.jumpinglines.utils.Const
import com.harsh.jumpinglines.utils.properties
import com.intellij.ide.BrowserUtil
import com.intellij.notification.*
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.options.ShowSettingsUtil

fun showNotification(message: String) {

    val notificationGroup = NotificationGroupManager.getInstance()
        .getNotificationGroup(Const.NOTIFICATION_PLUGIN_UPDATED)

    // Create a notification with the specified message and notification group
    val notification = notificationGroup.createNotification(
        title = Const.PLUGIN_NAME,
        content = message,
        type = NotificationType.INFORMATION // can be ERROR, WARNING, INFORMATION, IDE_UPDATE
    ).apply {
        if (message.contains("plugin updates")) { // Show plugin settings link only post update of plugin
            addAction(object : NotificationAction("Plugin settings") {
                override fun actionPerformed(event: AnActionEvent, notification: Notification) {
                    notification.expire() // Dismiss notification balloon
                    // Show plugin settings dialog
                    ShowSettingsUtil.getInstance().showSettingsDialog(event.project, JumpingLinesSettings::class.java)
                }
            })
        }
    }

    Notifications.Bus.notify(notification)

}

fun promptPluginReviewNotification() {
    val notificationGroup = NotificationGroupManager.getInstance()
        .getNotificationGroup(Const.NOTIFICATION_PLUGIN_REVIEW)

    val notificationRating = notificationGroup.createNotification(
        title = "Enjoying Jumping Lines?",
        content = "If you find this plugin helpful, please consider leaving a review!",
        type = NotificationType.INFORMATION
    ).apply {
        addAction(object : NotificationAction("Leave a Review") {
            override fun actionPerformed(e: AnActionEvent, notification: Notification) {
                BrowserUtil.browse(Const.PLUGIN_URL + "/reviews") // Open browser to the URL
                notification.expire() // Dismiss notification balloon
                properties().setValue(Const.HAS_PLUGIN_REVIEWED, true)
            }
        })
        addAction(object : NotificationAction("Maybe Later") {
            override fun actionPerformed(e: AnActionEvent, notification: Notification) {
                notification.expire() // Dismiss notification balloon
            }
        })
    }

    Notifications.Bus.notify(notificationRating)
}

/* Error prompts
"Looks like you hit the editor's wall."
"Can't jump out Let's stay inside the editor for now."
"Editor's got you grounded!"
"Looks like you hit the editor's wall."
"Jumping outside the editor isn't currently possible."
"Let's stay within the editor for now."
"If you'd like to jump to a different location, you can use the navigation bar within the editor."
"Need to jump somewhere else? Try using the navigation tools."
"Jumping outside the editor isn't supported for security reasons." (Only if appropriate)
"Looks like your jump jetpack is out of fuel! Let's stay in the editor for now." (Use cautiously and only if appropriate for the audience)
*/