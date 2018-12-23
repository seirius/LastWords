package com.lastwords.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.lastwords.LastWords

object DesktopLauncher {
    @JvmStatic
    fun main(arg: Array<String>) {
        val config = LwjglApplicationConfiguration()
        config.title = LastWords.TITLE
        config.width = LastWords.WIDTH
        config.height = LastWords.HEIGHT
        config.x = 1400
        config.y = 1500
        LwjglApplication(LastWords(), config)
    }
}
