package com.harsh.jumpinglines.utils

import com.intellij.ide.plugins.PluginManagerCore
import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.extensions.PluginId

val AnActionEvent.editor: Editor
    get() = this.getData(CommonDataKeys.EDITOR)
        ?: error("Editor is required but not available in this context.")

fun properties(): PropertiesComponent = PropertiesComponent.getInstance()

val lastNotificationShown: Long
    get() = properties().getLong(Const.LAST_RATING_PROMPT, 0L)

val hasPluginReviewed: Boolean
    get() = properties().getBoolean(Const.HAS_PLUGIN_REVIEWED, false)

fun Long.inHumanReadableForm(): String {
    return when {
        this >= 1_000_000_000 -> String.format("%.2fB", this / 1_000_000_000.0) // Billions
        this >= 1_000_000 -> String.format("%.2fM", this / 1_000_000.0) // Millions
        this >= 1_00_000 -> String.format("%.2fL", this / 1_00_000.0) // Lacs
        this >= 1_000 -> String.format("%.2fK", this / 1_000.0) // Thousands
        else -> this.toString() // Under 1000
    }
}

fun getPluginVersion(): String? {
    val pluginId = PluginId.getId("com.example.JumpingLines")
    val pluginDescriptor = PluginManagerCore.getPlugin(pluginId)

    return pluginDescriptor?.version
}