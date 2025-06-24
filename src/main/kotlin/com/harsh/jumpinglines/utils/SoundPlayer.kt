package com.harsh.jumpinglines.utils

import java.net.URL
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.Clip

object SoundPlayer {
    fun playJumpSound() {
        try {
            val soundUrl: URL = this::class.java.getResource("/sounds/jump.wav") ?: return
            val audioInput = AudioSystem.getAudioInputStream(soundUrl)
            val clip: Clip = AudioSystem.getClip()
            clip.open(audioInput)
            clip.start()
        } catch (ex: Exception) {
            ex.printStackTrace() // Fail silently or log if needed
        }
    }
}