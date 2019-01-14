package com.lastwords.states

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.lastwords.eventlistener.EventListener
import com.lastwords.eventlistener.LastEventEmitter

class MenuState(
        gameStateManager: GameStateManager,
        override val eventListener: EventListener
) : State(gameStateManager), LastEventEmitter {

    private val background1: Texture = Texture("hill/PNG/background1.png")
    private val background2: Texture = Texture("hill/PNG/background2.png")
    private val background3: Texture = Texture("hill/PNG/background3.png")
    private val background4: Texture = Texture("hill/PNG/background4.png")
    private val background5: Texture = Texture("hill/PNG/background5.png")
    private val background6: Texture = Texture("hill/PNG/background6.png")

    init {
        camera.setToOrtho(false, background1.width.toFloat(), background1.height.toFloat())
    }

    override fun handleInput() {
        if (Gdx.input.justTouched()) {
            gameStateManager.set(PlayState(gameStateManager, eventListener))
        }
    }

    override fun update(dt: Float) {
        handleInput()
    }

    override fun render(spriteBatch: SpriteBatch?) {
        spriteBatch!!.projectionMatrix = camera.combined
        spriteBatch.begin()
        spriteBatch.draw(background1, 0f, 0f)
        spriteBatch.draw(background2, 0f, 0f)
        spriteBatch.draw(background3, 0f, 0f)
        spriteBatch.draw(background4, 0f, 0f)
        spriteBatch.draw(background5, 0f, 0f)
        spriteBatch.draw(background6, 0f, 0f)
        spriteBatch.end()
    }

    override fun resize(width: Int, height: Int) {

    }

    override fun dispose() {
        background1.dispose()
        println("MenuState disposed")
    }
}
