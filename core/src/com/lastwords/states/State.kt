package com.lastwords.states

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.lastwords.LastWords

abstract class State(protected var gameStateManager: GameStateManager?) {

    protected var camera: OrthographicCamera = OrthographicCamera()
    protected var mousePosition: Vector2 = Vector2()

    protected abstract fun handleInput()
    abstract fun update(dt: Float)
    abstract fun render(spriteBatch: SpriteBatch?)
    abstract fun dispose()
    open fun resize(width: Int, height: Int) {
        camera.setToOrtho(false, width / LastWords.SCALE, height / LastWords.SCALE)
        gameStateManager!!.spriteBatch!!.projectionMatrix = camera.combined
    }

}
