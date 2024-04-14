package com.harsh.jumpinglines.utils

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.editor.Editor

val AnActionEvent.editor: Editor
	get() = this.getRequiredData(CommonDataKeys.EDITOR)