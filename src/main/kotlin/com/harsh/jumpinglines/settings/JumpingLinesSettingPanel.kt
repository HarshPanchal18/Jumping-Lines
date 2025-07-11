package com.harsh.jumpinglines.settings

import com.harsh.jumpinglines.utils.*
import com.harsh.jumpinglines.utils.Jumper.jumpScore
import com.intellij.openapi.actionSystem.Shortcut
import com.intellij.openapi.keymap.KeymapManager
import com.intellij.openapi.keymap.KeymapUtil
import com.intellij.ui.HyperlinkLabel
import com.intellij.ui.components.panels.VerticalLayout
import java.awt.FlowLayout
import javax.swing.*

class JumpingLinesSettingPanel {

    val parentPanel: JPanel
    private var forwardJumpRow: JPanel
    private var backwardJumpRow: JPanel
    private var scoreRow: JPanel
    private var hyperlinksRow: JPanel
    private var markerRow: JPanel
    private var soundRow: JPanel
    private var keymapPanel: JPanel

    private var titleLabel: JLabel
    private var scoreLabel: JLabel
    private var reviewLinkLabel: HyperlinkLabel
    private var reportLinkLabel: HyperlinkLabel
    private var markerCheckbox: JCheckBox
    private var soundCheckbox: JCheckBox

    private val forwardLineSpinner = JSpinner(
        SpinnerNumberModel(
            /* value = */ 0,
            /* minimum = */ 0,
            /* maximum = */ 50,
            /* stepSize = */ 1
        )
    )
    private val backwardLineSpinner = JSpinner(
        SpinnerNumberModel(
            /* value = */ 0,
            /* minimum = */ 0,
            /* maximum = */ 50,
            /* stepSize = */ 1
        )
    )

    init {

        val forwardJumpLayout = FlowLayout(/* align = */ FlowLayout.LEFT, /* hgap = */ 5, /* vgap = */ 2)
        forwardJumpRow = JPanel(forwardJumpLayout).apply {
            add(
                JLabel(runCatching { Icons.forwardJumpIcon }.getOrNull())
            )
            val forwardLabel = "<html>Number of lines to jump <b>forward:" + "&nbsp;".repeat(3) + "</b></html>"
            add(JLabel(forwardLabel))
            add(forwardLineSpinner)
        }

        val backwardJumpLayout = FlowLayout(/* align = */ FlowLayout.LEFT, /* hgap = */ 5, /* vgap = */ 2)
        backwardJumpRow = JPanel(backwardJumpLayout).apply {
            add(
                JLabel(runCatching { Icons.backwardJumpIcon }.getOrNull())
            )
            val backwardLabel = "<html>Number of lines to jump <b>backward:</b></html>"
            add(JLabel(backwardLabel))
            add(backwardLineSpinner)
        }

        val jumpScoreLayout = FlowLayout(/* align = */ FlowLayout.LEFT, /* hgap = */ 5, /* vgap = */ 2)

        titleLabel = JLabel("<html>Your progress is <b>remarkable!</b></html>").apply {
            icon = runCatching { Icons.scoreIcon }.getOrNull()
        }
        scoreLabel = JLabel("<html>You've jumped over <b>~${jumpScore.inHumanReadableForm()}</b> lines.</html>").apply {
            icon = runCatching { Icons.lightningIcon }.getOrNull()
        }

        scoreRow = JPanel(jumpScoreLayout).apply {
            add(titleLabel)
            add(scoreLabel)
        }

        val linksLayout = FlowLayout(/* align = */ FlowLayout.LEFT, /* hgap = */ 5, /* vgap = */ 2)
        reviewLinkLabel =
            HyperlinkLabel("Enjoying this plugin?").apply { setHyperlinkTarget(Const.PLUGIN_URL + "/reviews") }

        reportLinkLabel =
            HyperlinkLabel("Spotted an issue?").apply { setHyperlinkTarget(Const.PLUGIN_REPO_URL + "/issues") }

        hyperlinksRow = JPanel(linksLayout).apply {
            add(reviewLinkLabel)
            add(JLabel("|"))
            add(reportLinkLabel)
        }

        val markerLayout = FlowLayout(/* align = */ FlowLayout.LEFT)
        markerCheckbox = JCheckBox("Show markers to guide next jump?").apply {
            isSelected = properties().getBoolean(Const.IS_MARKER_ENABLED, true)
            addActionListener {
                properties().setValue(Const.IS_MARKER_ENABLED, isSelected)
            }
        }

        markerRow = JPanel(markerLayout).apply {
            add(JLabel().apply { icon = runCatching { Icons.guideIcon }.getOrNull() })
            add(markerCheckbox)
        }

        val soundLayout = FlowLayout(/* align = */ FlowLayout.LEFT)
        soundCheckbox = JCheckBox("Play sound on jump?").apply {
            isSelected = properties().getBoolean(Const.IS_SOUND_ENABLED)
            addActionListener {
                properties().setValue(Const.IS_SOUND_ENABLED, isSelected)
            }
        }

        soundRow = JPanel(soundLayout).apply {
            add(JLabel().apply { icon = runCatching { Icons.soundIcon }.getOrNull() })
            add(soundCheckbox)
        }

        keymapPanel = JPanel().apply {
            layout = BoxLayout(this, BoxLayout.Y_AXIS)
            border = BorderFactory.createTitledBorder(
                "<html>" +
                        "<b><u>Keymaps & Shortcuts</b></u> " +
                        "<i>(v${getPluginVersion()})</i>" +
                        "</html>"
            )

            add(shortcutRow("com.harsh.jumpinglines.jumps.JumpForwardLines", "Jump in Forward"))
            add(shortcutRow("com.harsh.jumpinglines.jumps.JumpBackwardLines", "Jump in Backward"))
            add(
                shortcutRow(
                    "com.harsh.jumpinglines.jumps.selection.JumpForwardSelected",
                    "Jump in Forward with Selection"
                )
            )
            add(
                shortcutRow(
                    "com.harsh.jumpinglines.jumps.selection.JumpBackwardSelected",
                    "Jump in Backward with Selection"
                )
            )
            add(shortcutRow("com.harsh.jumpinglines.jumps.middle.JumpOnMiddle", "Jump on Middle of editor"))
            add(
                shortcutRow(
                    "com.harsh.jumpinglines.jumps.cursortrace.CursorTraceForward",
                    "Replicate Cursor in Forward"
                )
            )
            add(
                shortcutRow(
                    "com.harsh.jumpinglines.jumps.cursortrace.CursorTraceBackward",
                    "Replicate Cursor in Backward"
                )
            )
        }

        val parentLayout = VerticalLayout(/* gap = */ 2,/* alignment = */ SwingConstants.LEFT)
        parentPanel = JPanel(parentLayout).apply {
            add(backwardJumpRow)
            add(forwardJumpRow)
            add(markerRow)
            add(soundRow)
            add(scoreRow)
            add(keymapPanel)
            add(hyperlinksRow)
        }

    }

    fun getForwardLinesValue(): Int = forwardLineSpinner.value as Int

    fun getBackwardLinesValue(): Int = backwardLineSpinner.value as Int

    fun getMarkerState(): Boolean = markerCheckbox.isSelected

    fun getSoundState(): Boolean = markerCheckbox.isSelected

    fun setValues(forwardLine: Int, backwardLine: Int, isMarkerEnabled: Boolean, isSoundEnabled: Boolean) {
        forwardLineSpinner.value = forwardLine
        backwardLineSpinner.value = backwardLine
        markerCheckbox.isSelected = isMarkerEnabled
        soundCheckbox.isSelected = isSoundEnabled
    }

    private fun shortcutRow(actionId: String, description: String): JLabel {
        val pluginShortcuts: Array<Shortcut> = KeymapManager.getInstance()
            .activeKeymap
            .getShortcuts(actionId)

        val shortcutKey: String = KeymapUtil.getShortcutsText(pluginShortcuts)
            .replace(".", "Period")
            .replace(";", "Semicolon")

        return JLabel(
            "<html>" +
                    "<b>${description}:</b>\t\t<i>${shortcutKey.ifBlank { "Not Assigned" }}</i>" +
                    "</html>"
        ).apply {
            border = BorderFactory.createEmptyBorder(/* top = */ 6, /* left = */ 6, /* bottom = */ 4, /* right = */ 6)
        }

    }

}