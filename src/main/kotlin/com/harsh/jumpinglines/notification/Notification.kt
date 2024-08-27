package com.harsh.jumpinglines.notification

import com.harsh.jumpinglines.utils.Const
import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.updateSettings.impl.pluginsAdvertisement.notificationGroup
import javax.swing.event.HyperlinkEvent

fun showNotification(message: String, listener: (Notification?, HyperlinkEvent?) -> Unit = { notification, event -> }) {
    // Create a notification with the specified message and notification group
    val notification = notificationGroup.createNotification(
        title = Const.PLUGIN_NAME,
        content = message,
        type = NotificationType.INFORMATION // The notification type (ERROR, WARNING, INFORMATION, IDE_UPDATE)
    ).setListener { notification, hyperlinkEvent -> listener(notification, hyperlinkEvent) }
    Notifications.Bus.notify(notification)
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