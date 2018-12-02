package com.lastwords

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.lastwords.states.GameStateManager
import com.lastwords.states.PlayState

class LastWords : ApplicationAdapter() {

    private var spriteBatch: SpriteBatch = SpriteBatch()
    private var gameStateManager: GameStateManager = GameStateManager(spriteBatch)

    override fun create() {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        gameStateManager.push(PlayState(gameStateManager))
        //        gameStateManager.push(new MenuState(gameStateManager));
    }

    override fun render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        gameStateManager.update(Gdx.graphics.deltaTime)
        gameStateManager.render(spriteBatch)
    }

    override fun resize(width: Int, height: Int) {
        gameStateManager.resize(width, height)
    }

    companion object {
        const val TITLE = "LastWords"
        const val WIDTH = 1200
        const val HEIGHT = 720
        const val SCALE = 1.8f
    }
}
