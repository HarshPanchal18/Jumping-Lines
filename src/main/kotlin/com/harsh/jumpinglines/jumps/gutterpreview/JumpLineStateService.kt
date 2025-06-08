package com.harsh.jumpinglines.jumps.gutterpreview

import com.intellij.openapi.components.Service

@Service(Service.Level.PROJECT)
class JumpLineStateService {
    var forwardLine: Int? = null
    var backwardLine: Int? = null

    fun setBoundary(forwardLine: Int?, backwardLine: Int?) {
        this.forwardLine = forwardLine
        this.backwardLine = backwardLine
    }
}