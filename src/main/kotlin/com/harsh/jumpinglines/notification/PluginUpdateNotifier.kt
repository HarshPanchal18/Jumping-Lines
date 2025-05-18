package com.harsh.jumpinglines.notification

import com.harsh.jumpinglines.utils.*
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity

class PluginUpdateNotifier : StartupActivity.DumbAware {
    override fun runActivity(project: Project) {

        // Get versions
        val previousKnownVersion = properties().getValue(Const.PREVIOUS_KNOWN_VERSION)
        val currentVersion = getPluginVersion()

        if (previousKnownVersion != currentVersion) { // Version mismatched
            promptPluginReviewNotification() // Ask for rating and reviews

            showNotification(message = "<b>New plugin updates are applied.</b>")
            properties().setValue(Const.PREVIOUS_KNOWN_VERSION, currentVersion) // Update value
        }

        val now: Long = System.currentTimeMillis()
        val oneWeekInMillis: Long = 60 * 60 * 24 * 7 * 1000L
        val numberOfWeeksToRemindAfter = 2

        if (!hasPluginReviewed && ((now - lastNotificationShown) > (oneWeekInMillis * numberOfWeeksToRemindAfter))) {
            promptPluginReviewNotification()
            properties().setValue(Const.LAST_RATING_PROMPT, now.toString())
            println(now)
        }
    }
}