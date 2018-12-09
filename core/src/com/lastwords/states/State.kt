package com.lastwords.states

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.lastwords.LastWords

abstract class State(protected var gameStateManager: GameStateManager) {

    protected var camera: OrthographicCamera = OrthographicCamera()
    protected var guiCamera: OrthographicCamera = OrthographicCamera()

    protected abstract fun handleInput()
    abstract fun update(dt: Float)
    abstract fun render(spriteBatch: SpriteBatch?)
    abstract fun dispose()
    open fun resize(width: Int, height: Int) {
        CURRENT_WIDTH = width / LastWords.SCALE
        CURRENT_HEIGHT = height / LastWords.SCALE
        CURRENT_SIZE.x = CURRENT_WIDTH
        CURRENT_SIZE.y = CURRENT_HEIGHT
        camera.setToOrtho(false, CURRENT_WIDTH, CURRENT_HEIGHT)
        guiCamera.setToOrtho(false, CURRENT_WIDTH, CURRENT_HEIGHT)
        gameStateManager.spriteBatch.projectionMatrix = camera.combined
    }
    fun getWorldMousePosition(): Vector2 {
        val position = camera.unproject(Vector3(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f))
        return Vector2(position.x, position.y)
    }

    companion object {
        var CURRENT_SIZE: Vector2 = Vector2(LastWords.WIDTH.toFloat(), LastWords.HEIGHT.toFloat())
        var CURRENT_WIDTH: Float = LastWords.WIDTH.toFloat()
        var CURRENT_HEIGHT: Float = LastWords.HEIGHT.toFloat()
    }

}
