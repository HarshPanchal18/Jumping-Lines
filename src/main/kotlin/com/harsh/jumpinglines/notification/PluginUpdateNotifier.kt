package com.harsh.jumpinglines.notification

import com.harsh.jumpinglines.utils.*
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity

class PluginUpdateNotifier : StartupActivity.DumbAware {
    override fun runActivity(project: Project) {

        val previousKnownVersion = properties().getValue(Const.PREVIOUS_KNOWN_VERSION)
        val currentVersion = getPluginVersion()

        if (previousKnownVersion != currentVersion) { // Version mismatched
            promptPluginReviewNotification()

            showNotification(message = "<b>New plugin updates are applied.</b>")
            properties().setValue(Const.PREVIOUS_KNOWN_VERSION, currentVersion)
        }

        val now: Long = System.currentTimeMillis()
        val oneWeekInMillis = 60 * 60 * 24 * 7 * 1000L

        if (!hasPluginReviewed && now - lastNotificationShown > oneWeekInMillis) {
            promptPluginReviewNotification()
            properties().setValue(Const.LAST_RATING_PROMPT, now.toString())
            println(now)
        }
    }
}