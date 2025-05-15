package com.harsh.jumpinglines.notification

import com.harsh.jumpinglines.utils.Const
import com.harsh.jumpinglines.utils.getPluginVersion
import com.harsh.jumpinglines.utils.properties
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity

class PluginUpdateNotifier : ProjectActivity {
    override suspend fun execute(project: Project) {

        val previousKnownVersion = properties().getValue(Const.PREVIOUS_KNOWN_VERSION)
        val currentVersion = getPluginVersion()

        if (previousKnownVersion != currentVersion) { // Version mismatched
            promptPluginReviewNotification()

            showNotification(message = "<b>New plugin updates are applied. Jump more higher.</b>")
            properties().setValue(Const.PREVIOUS_KNOWN_VERSION, currentVersion)
        }
    }
}